package com.cx.service

import com.cx.domain.Disk
import com.cx.domain.Host
import com.cx.domain.Vcenter
import com.cx.domain.VirtualMachine
import com.vmware.vim25.HostScsiDisk
import com.vmware.vim25.HostScsiTopology
import com.vmware.vim25.HostScsiTopologyInterface
import com.vmware.vim25.HostScsiTopologyLun
import com.vmware.vim25.HostScsiTopologyTarget
import com.vmware.vim25.HostStorageDeviceInfo
import com.vmware.vim25.HostSystemConnectionState
import com.vmware.vim25.VmfsDatastoreInfo
import com.vmware.vim25.mo.ClusterComputeResource
import com.vmware.vim25.mo.Datastore
import com.vmware.vim25.mo.HostStorageSystem
import com.vmware.vim25.mo.HostSystem
import com.vmware.vim25.mo.InventoryNavigator
import com.vmware.vim25.mo.ManagedEntity
import com.vmware.vim25.mo.ResourcePool
import com.vmware.vim25.mo.ServiceInstance
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.StackTraceUtils

@Transactional
class VcenterService {

    ConfigurationService configurationService
    VcenterConnectionService vcenterConnectionService
    public Boolean manualConnectionManagement = false // Use to keep a connection open

    private String buildVcenterUrl(String ip) {
        configurationService.getVcenterUrl().replace("\$ip", ip)
    }

    public ServiceInstance openConnection(Vcenter vcenter) {
        vcenterConnectionService.createOrGetShareableServiceInstance(buildVcenterUrl(vcenter.ip), vcenter.credential.username, vcenter.credential.password)
    }

    public void closeConnection(Vcenter vcenter) {
        vcenterConnectionService.destroyShareableServiceInstance(buildVcenterUrl(vcenter.ip), vcenter.credential.username)
    }

    public boolean verifyConnection(Vcenter vcenter) {
        try {
            ServiceInstance serviceInstance = openConnection(vcenter)
            String apiVersion = serviceInstance?.serviceContent?.about?.apiVersion?.trim()
            log.info "Connection established to vCenter $vcenter version $apiVersion"
            apiVersion != null
        } catch(Exception e) {
            log.error("Connection to vCenter ${vcenter.ip} failed $e")
            false
        } finally {
            closeConnection(vcenter)
        }
    }

    private Collection<VirtualMachine> checkResourcePoolForVms(ClusterComputeResource clusterComputeResource, ResourcePool resourcePool, Collection<VirtualMachine> virtualMachines) {
        log.info "Inspect resource pool ${resourcePool.name} for virtual machines"
        resourcePool.VMs?.each { com.vmware.vim25.mo.VirtualMachine vm ->
            log.info "Found virtual machine ${vm.name} in resource pool ${resourcePool.name}"
            VirtualMachine virtualMachine = new VirtualMachine()
            virtualMachine.name = vm.name
            virtualMachine.key = vm.MOR.val
            virtualMachine.powerState = formatStateString(vm.runtime?.powerState?.toString())
            virtualMachine.template = vm.config?.template

            String hostKey = vm.runtime?.host?.val
            if(hostKey) {
                ManagedEntity[] hostSystems = new InventoryNavigator(clusterComputeResource).searchManagedEntities("HostSystem")
                HostSystem hostSystem = (HostSystem) hostSystems?.find{it.MOR.val == hostKey}
                virtualMachine.host = hostSystem?.name
            }

            virtualMachines << virtualMachine
        }

//        resourcePool?.resourcePools?.each { ResourcePool childResourcePool ->
//            checkResourcePoolForVms(clusterComputeResource, childResourcePool, virtualMachines)
//        }

        virtualMachines
    }

    private Collection<VirtualMachine> getVirtualMachinesInternal(ServiceInstance serviceInstance) throws Exception {
        Collection<VirtualMachine> virtualMachines = []
        ManagedEntity[] clusterComputeResources = new InventoryNavigator(serviceInstance.rootFolder).searchManagedEntities("ClusterComputeResource")
        clusterComputeResources.each { ClusterComputeResource clusterComputeResource ->
            String clusterName = clusterComputeResource.name
            log.info "Inspect cluster $clusterName for virtual machines"
            ResourcePool resourcePool = clusterComputeResource.resourcePool
            if(resourcePool) {
                Collection<VirtualMachine> resourcePoolVirtualMachines = checkResourcePoolForVms(clusterComputeResource, resourcePool, [])
                log.info("Found ${resourcePoolVirtualMachines.size()} VMs ${resourcePoolVirtualMachines}")
                log.info "Cluster $clusterName has ${resourcePoolVirtualMachines.size()} virtual machines"
                resourcePoolVirtualMachines.each { VirtualMachine virtualMachine ->
                    virtualMachine.cluster = clusterName
                }
                virtualMachines.addAll(resourcePoolVirtualMachines)
            }
        }
        virtualMachines
    }

    public void persistVirtualMachines(Vcenter vcenter, Collection<VirtualMachine> virtualMachines) {
        log.info "Persist ${virtualMachines.size()} virtual machines for vCenter $vcenter"
        vcenter.virtualMachines*.delete()
        vcenter.virtualMachines.clear()
        virtualMachines.each { VirtualMachine virtualMachine ->
            vcenter.addToVirtualMachines(virtualMachine)
            virtualMachine.vcenter = vcenter
            virtualMachine.save()
        }
        vcenter.save()
    }

    public Collection<VirtualMachine> getVirtualMachines(Vcenter vcenter, boolean persist = true) throws Exception {
        try {
            ServiceInstance serviceInstance = openConnection(vcenter)
            Collection<VirtualMachine> virtualMachines = getVirtualMachinesInternal(serviceInstance)
            if(persist) {
                persistVirtualMachines(vcenter, virtualMachines)
                vcenter.virtualMachines
            } else {
                virtualMachines
            }
        } catch(Exception e) {
            log.error "Error getting virtual machines for vCenter ${vcenter.ip} $e"
            throw e
        } finally {
            if(!manualConnectionManagement) closeConnection(vcenter)
        }
    }

    private String formatStateString(String state) {
        if(!state || state == "") ""

        String formattedState = state?.substring(0,1)?.toUpperCase() + state?.substring(1)
        String spacedFormattedState = formattedState
        formattedState.chars.each { Character character ->
            if(character.isUpperCase()) {
                spacedFormattedState = formattedState.replaceAll(character.toString(), " ${character.toString()}")
            }
        }
        spacedFormattedState.trim()
    }

    private Collection<Host> getHostsInternal(ServiceInstance serviceInstance) throws Exception {
        Collection<Host> hosts = []
        ManagedEntity[] clusterComputeResources = new InventoryNavigator(serviceInstance.rootFolder).searchManagedEntities("ClusterComputeResource")
        clusterComputeResources.each { ClusterComputeResource clusterComputeResource ->
            ManagedEntity[] hostSystems = new InventoryNavigator(clusterComputeResource).searchManagedEntities("HostSystem")
            hostSystems.each { HostSystem hostSystem ->
                Host host = new Host()
                host.name = hostSystem.name
                host.key = hostSystem.MOR.val
                host.os = hostSystem?.getPropertyByPath("config.product.version") ?: "N/A"
                host.connectionState = formatStateString(hostSystem?.runtime?.connectionState?.toString())
                host.powerState = formatStateString(hostSystem?.runtime?.powerState?.toString())
                host.maintenanceMode = hostSystem.runtime.inMaintenanceMode
                host.cluster = clusterComputeResource.name
                hosts << host
            }
        }
        hosts
    }

    public void persistHosts(Vcenter vcenter, Collection<Host> hosts) {
        log.info "Persist ${hosts.size()} hosts for vCenter $vcenter"
        vcenter.hosts*.delete()
        vcenter.hosts.clear()
        hosts.each { Host host ->
            vcenter.addToHosts(host)
            host.vcenter = vcenter
            host.save()
        }
        vcenter.save()
    }

    public Collection<Host> getHosts(Vcenter vcenter, boolean persist = true) throws Exception {
        try {
            ServiceInstance serviceInstance = openConnection(vcenter)
            Collection<Host> hosts = getHostsInternal(serviceInstance)
            if(persist) {
                persistHosts(vcenter, hosts)
                vcenter.hosts
            } else {
                hosts
            }
        } catch(Exception e) {
            log.error "Error getting hosts for vCenter ${vcenter.ip} $e"
            throw e
        } finally {
            if(!manualConnectionManagement) closeConnection(vcenter)
        }
    }

    private Integer findLun(HostSystem hostSystem, HostScsiDisk hostScsiDisk) throws Exception {
        //log.info("Attempt to find LUN for HostScsiDisk device path ${hostScsiDisk?.devicePath} uuid ${hostScsiDisk?.uuid}")
        Integer lun
        try {
            HostStorageSystem hostStorageSystem = hostSystem.getHostStorageSystem();
            HostStorageDeviceInfo hostStorageDeviceInfo = hostStorageSystem.getStorageDeviceInfo();
            HostScsiTopology hostScsiTopology = hostStorageDeviceInfo.getScsiTopology();
            HostScsiTopologyInterface[] hostScsiTopologyInterfaces = hostScsiTopology.getAdapter();
            hostScsiTopologyInterfaces.each { HostScsiTopologyInterface hostScsiTopologyInterface ->
                hostScsiTopologyInterface.getTarget().each { HostScsiTopologyTarget hostScsiTopologyTarget ->
                    //log.info("HostScsiTopologyTarget ${hostScsiTopologyTarget?.key}")
                    hostScsiTopologyTarget.lun.each { HostScsiTopologyLun hostScsiTopologyLun ->
                        String[] parts = hostScsiTopologyLun.key.split("-")
                        // log.info("Parsed out uuid $parts lun ${hostScsiTopologyLun.lun}")
                        String uuid = parts[parts.length - 1]
                        if(hostScsiDisk?.uuid == uuid) {
                            //log.info("Found LUN ${hostScsiTopologyLun.lun}")
                            lun = hostScsiTopologyLun.lun
                        }
                    }
                }
            }
        } catch(Exception e) {
            log.error("Error navigating host storage system APIs to retrieve LUN", e)
            throw new Exception("Error navigating host storage system APIs to retrieve LUN")
        }
        if(lun == null) {
            log.error("LUN not found for disk ${hostScsiDisk.uuid}")
            throw new Exception("LUN not found for disk ${hostScsiDisk.uuid}")
        }
        lun
    }

    private Collection<Disk> getDisksInternal(ServiceInstance serviceInstance) throws Exception {
        try {
            Collection<Disk> disks = []
            ManagedEntity[] clusterComputeResources = new InventoryNavigator(serviceInstance.rootFolder).searchManagedEntities("ClusterComputeResource")
            clusterComputeResources.each { ClusterComputeResource clusterComputeResource ->
                ManagedEntity[] hostSystems = new InventoryNavigator(clusterComputeResource).searchManagedEntities("HostSystem")
                for(HostSystem hostSystem : hostSystems) {
                    String hostname = hostSystem.name
                    log.info("Find disks for host $hostname")
                    if(hostSystem?.runtime?.connectionState != HostSystemConnectionState.connected) {
                        log.info("Host $hostname not in connected state thus disk information cannot be collected")
                        continue
                    }
                    HostStorageSystem hostStorageSystem = hostSystem.getHostStorageSystem()
                    HostStorageDeviceInfo hostStorageDeviceInfo = hostStorageSystem.getStorageDeviceInfo()
                    HostScsiDisk[] hostScsiDisks = hostStorageDeviceInfo.getScsiLun().findAll{ it instanceof HostScsiDisk }
                    hostScsiDisks.each { HostScsiDisk hostScsiDisk ->
                        Disk disk = new Disk()
                        Integer lun = findLun(hostSystem, hostScsiDisk)
                        disk.lun = lun
                        String diskUuid = hostScsiDisk.deviceName.contains("naa.") ? hostScsiDisk.deviceName.split("naa.")[1] : null
                        disk.uuid = diskUuid
                        disk.capacity = (hostScsiDisk.capacity.block * hostScsiDisk.capacity.blockSize / (1024 * 1024 * 1024))
                        disk.devicePath = hostScsiDisk.devicePath
                        disk.host = hostname
                        disk.cluster = clusterComputeResource.name
                        disk.name = "${hostname}_$lun"
                        disks << disk
                    }

                    Datastore[] datastores = hostSystem.datastores
                    datastores?.each { Datastore datastore ->
                        if(datastore.info instanceof VmfsDatastoreInfo) {
                            VmfsDatastoreInfo vmfsDatastoreInfo = datastore.info
                            String diskName = vmfsDatastoreInfo.vmfs.extent[0].diskName
                            log.info("Found datastore ${datastore.MOR.val} on disk $diskName")
                            String devicePath = "/vmfs/devices/disks/$diskName"

                            Disk disk = disks.find{it.devicePath == devicePath}
                            if(disk) {
                                disk.datastore = datastore.name
                                disk.key = datastore.MOR.val
                                disk.datastoreCapacity = (vmfsDatastoreInfo.vmfs.capacity / (1024 * 1024 * 1024))
                                disk.datastoreVersion = vmfsDatastoreInfo.vmfs.majorVersion
                                disk.datastoreBlockSize = vmfsDatastoreInfo.vmfs.blockSizeMb
                                log.info("Datastore ${disk.datastore} capacity ${disk.datastoreCapacity} found on disk of capacity ${disk.capacity}")
                            }
                        }
                    }
                    /*
                    TODO FILE / NFS INFO
                     */
                    log.info("Host $hostname has disks $disks")
                }
            }
            disks
        } catch(Exception e) {
            log.error "Error getting disks $e"
            throw e
        }

    }

    public void persistDisks(Vcenter vcenter, Collection<Disk> disks) {
        log.info "Persist ${disks.size()} disks for vCenter $vcenter"
        vcenter.disks*.delete()
        vcenter.disks.clear()
        disks.each { Disk disk ->
            vcenter.addToDisks(disk)
            disk.vcenter = vcenter
            disk.save()
        }
        vcenter.save()
    }

    public Collection<Disk> getDisks(Vcenter vcenter, boolean persist = true) throws Exception {
        try {
            ServiceInstance serviceInstance = openConnection(vcenter)
            Collection<Disk> disks = getDisksInternal(serviceInstance)
            if(persist) {
                persistDisks(vcenter, disks)
                vcenter.disks
            } else {
                disks
            }
        } catch(Exception e) {
            log.error "Error getting hosts for vCenter ${vcenter.ip}"
//            StackTraceUtils.printSanitizedStackTrace(e)
            throw e
        } finally {
            if(!manualConnectionManagement) closeConnection(vcenter)
        }
    }
}
