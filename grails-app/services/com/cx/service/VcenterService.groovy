package com.cx.service

import com.cx.domain.Host
import com.cx.domain.Vcenter
import com.cx.domain.VirtualMachine
import com.vmware.vim25.mo.ClusterComputeResource
import com.vmware.vim25.mo.HostSystem
import com.vmware.vim25.mo.InventoryNavigator
import com.vmware.vim25.mo.ManagedEntity
import com.vmware.vim25.mo.ResourcePool
import com.vmware.vim25.mo.ServiceInstance
import grails.transaction.Transactional

@Transactional
class VcenterService {

    ConfigurationService configurationService
    VcenterConnectionService vcenterConnectionService

    private String buildVcenterUrl(String ip) {
        configurationService.getVcenterUrl().replace("\$ip", ip)
    }

    private ServiceInstance openConnection(Vcenter vcenter) {
        vcenterConnectionService.createOrGetShareableServiceInstance(buildVcenterUrl(vcenter.ip), vcenter.credential.username, vcenter.credential.password)
    }

    private void closeConnection(Vcenter vcenter) {
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

    public Collection<VirtualMachine> getVirtualMachines(Vcenter vcenter) {
        try {
            Collection<VirtualMachine> virtualMachines = []
            ServiceInstance serviceInstance = openConnection(vcenter)
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
                        virtualMachine.save()
                    }
                    virtualMachines.addAll(resourcePoolVirtualMachines)
                }
            }
            virtualMachines
        } catch(Exception e) {
            log.error "Error getting virtual machines for vCenter ${vcenter.ip} $e"
            throw e
        } finally {
            closeConnection(vcenter)
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

    /*
    TODO - Update domain and add assoc to vCenter. Remember to clear old list before re-loading
     */
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

    /*
    TODO - Update domain and add assoc to vCenter. Remember to clear old list before re-loading
     */
    public Collection<Host> getHosts(Vcenter vcenter) {
        try {
            Collection<Host> hosts = []
            ServiceInstance serviceInstance = openConnection(vcenter)
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
                    host.save()
                    hosts << host
                }
            }
            hosts
        } catch(Exception e) {
            log.error "Error getting hosts for vCenter ${vcenter.ip} $e"
            throw e
        } finally {
            closeConnection(vcenter)
        }
    }
}
