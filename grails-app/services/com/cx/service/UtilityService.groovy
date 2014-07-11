package com.cx.service

import com.cx.domain.Blade
import com.cx.domain.Credential
import com.cx.domain.Disk
import com.cx.domain.Host
import com.cx.domain.NxOsSwitch
import com.cx.domain.Port
import com.cx.domain.Search
import com.cx.domain.SearchResult
import com.cx.domain.Server
import com.cx.domain.Ucs
import com.cx.domain.Vcenter
import com.cx.domain.VirtualMachine
import com.cx.domain.Vlan
import com.cx.domain.Vsan
import com.cx.domain.Zone
import com.cx.domain.Zoneset
import grails.async.Promise

import static grails.async.Promises.*
import grails.transaction.Transactional

@Transactional
class UtilityService {
    ConfigurationService configurationService
    UcsService ucsService
    NxOsSwitchService nxOsSwitchService
    VcenterService vcenterService

    private void deleteDeviceData() {
        Ucs.list().each { Ucs ucs ->
            def deleteBlades = []
            deleteBlades.addAll(ucs.blades)
            deleteBlades.each {
                ucs.removeFromBlades(it)
                it.delete(flush: true)
            }
            def deleteServers = []
            deleteServers.addAll(ucs.servers)
            deleteServers.each {
                ucs.removeFromServers(it)
                it.delete(flush: true)
            }
            def deleteVlans = []
            deleteVlans.addAll(ucs.vlans)
            deleteVlans.each {
                ucs.removeFromVlans(it)
                it.delete(flush: true)
            }
            def deleteVsans = []
            deleteVsans.addAll(ucs.vsans)
            deleteVsans.each {
                ucs.removeFromVsans(it)
                it.delete(flush: true)
            }
        }

        NxOsSwitch.list().each { NxOsSwitch nxOsSwitch ->
            def deleteZonesets = []
            deleteZonesets.addAll(nxOsSwitch.zonesets)
            deleteZonesets.each {
                nxOsSwitch.removeFromZonesets(it)
                it.delete(flush: true)
            }
            def deleteVsans = []
            deleteVsans.addAll(nxOsSwitch.vsans)
            deleteVsans.each {
                nxOsSwitch.removeFromVsans(it)
                it.delete(flush: true)
            }
        }

        Vcenter.list().each { Vcenter vcenter ->
            def deleteHosts = []
            deleteHosts.addAll(vcenter.hosts)
            deleteHosts.each {
                vcenter.removeFromHosts(it)
                it.delete(flush: true)
            }
            def deleteDisks = []
            deleteDisks.addAll(vcenter.disks)
            deleteDisks.each {
                vcenter.removeFromDisks(it)
                it.delete(flush: true)
            }
            def deleteVirtualMachines = []
            deleteVirtualMachines.addAll(vcenter.virtualMachines)
            deleteVirtualMachines.each {
                vcenter.removeFromVirtualMachines(it)
                it.delete(flush: true)
            }
        }    
    }

    @Transactional
    def loadDeviceData(boolean retry = true) {
        try {
            // Clean old data with flush to avoid bullshit GORM errors
            deleteDeviceData()

            def tasks = []

            // UCS
            ucsService.manualConnectionManagement = true
            Ucs.list().each { Ucs ucs ->
                ucsService.createOrGetSession(ucs)
                ucsService.createOrGetRestClient(ucs)
            }
            Map<Ucs,Collection<Blade>> ucsBlades = [:]
            Map<Ucs,Collection<Blade>> ucsServers = [:]
            Map<Ucs,Collection<Blade>> ucsVlans = [:]
            Map<Ucs,Collection<Blade>> ucsVsans = [:]
            // Initiate UCS async tasks
//            Ucs.list().each { Ucs ucs ->
//                Promise promise = task {
//                    ucsBlades.put(ucs, ucsService.getBlades(ucs, false))
//                    ucsServers.put(ucs, ucsService.getServers(ucs, false))
//                    ucsVlans.put(ucs, ucsService.getVlans(ucs, false))
//                    ucsVsans.put(ucs, ucsService.getVsans(ucs, false))
//                }
//                tasks << promise
//            }
            Ucs.list().each { Ucs ucs ->
                tasks << task { ucsBlades.put(ucs, ucsService.getBlades(ucs, false)) }
                tasks << task { ucsServers.put(ucs, ucsService.getServers(ucs, false)) }
                tasks << task { ucsVlans.put(ucs, ucsService.getVlans(ucs, false)) }
                tasks << task { ucsVsans.put(ucs, ucsService.getVsans(ucs, false)) }
            }

            // NX OS Switch
//            Map<NxOsSwitch,Collection<Zoneset>> nxOsSwitchZones = [:]
//            Map<NxOsSwitch,Collection<Vsan>> nxOsSwitchVsans = [:]
//            // Initiate NX OS Switch async tasks
////            NxOsSwitch.list().each { NxOsSwitch nxOsSwitch ->
////                Promise promise = task {
////                    nxOsSwitchZones.put(nxOsSwitch, nxOsSwitchService.getZones(nxOsSwitch, false))
////                    nxOsSwitchVsans.put(nxOsSwitch, nxOsSwitchService.getVsans(nxOsSwitch, false))
////                }
////                tasks << promise
////            }
//            NxOsSwitch.list().each { NxOsSwitch nxOsSwitch ->
//                tasks << task { nxOsSwitchZones.put(nxOsSwitch, nxOsSwitchService.getZones(nxOsSwitch, false)) }
//                tasks << task { nxOsSwitchVsans.put(nxOsSwitch, nxOsSwitchService.getVsans(nxOsSwitch, false)) }
//            }
//
//            // vCenter
//            vcenterService.manualConnectionManagement = true
//            Vcenter.list().each { Vcenter vcenter ->
//                vcenterService.openConnection(vcenter)
//            }
//            Map<Vcenter,Collection<VirtualMachine>> vcenterVirtualMachines = [:]
//            Map<Vcenter,Collection<Host>> vcenterHosts = [:]
//            Map<Vcenter,Collection<Disk>> vcenterDisks = [:]
//            // Initiate vCenter async tasks
////            Vcenter.list().each { Vcenter vcenter ->
////                Promise promise = task {
////                    vcenterVirtualMachines.put(vcenter, vcenterService.getVirtualMachines(vcenter, false))
////                    vcenterHosts.put(vcenter, vcenterService.getHosts(vcenter, false))
////                    vcenterDisks.put(vcenter, vcenterService.getDisks(vcenter, false))
////                }
////                tasks << promise
////            }
//            Vcenter.list().each { Vcenter vcenter ->
//                tasks << task { vcenterVirtualMachines.put(vcenter, vcenterService.getVirtualMachines(vcenter, false)) }
//                tasks << task { vcenterHosts.put(vcenter, vcenterService.getHosts(vcenter, false)) }
//                tasks << task { vcenterDisks.put(vcenter, vcenterService.getDisks(vcenter, false)) }
//            }


            // Wait for all device data tasks
            waitAll(tasks)
            log.info "Done waiting for ${tasks.size()} tasks"

            // Persist
            // UCS
            ucsBlades.each { Ucs ucs, Collection<Blade> blades ->
                ucsService.persistBlades(ucs.refresh(), blades)
            }
            ucsServers.each { Ucs ucs, Collection<Server> servers ->
                ucsService.persistServers(ucs.refresh(), servers)
            }
            ucsVlans.each { Ucs ucs, Collection<Vlan> vlans ->
                ucsService.persistVlans(ucs.refresh(), vlans)
            }
            ucsVsans.each { Ucs ucs, Collection<Vlan> vsans ->
                ucsService.persistVsans(ucs.refresh(), vsans)
            }
            ucsService.manualConnectionManagement = false
            Ucs.list().each { Ucs ucs ->
                ucsService.destroySession(ucs)
            }
            // NX-OS Switch
//            nxOsSwitchZones.each { NxOsSwitch nxOsSwitch, Collection<Zoneset> zones ->
//                nxOsSwitchService.persistZones(nxOsSwitch.refresh(), zones)
//            }
//            nxOsSwitchVsans.each { NxOsSwitch nxOsSwitch, Collection<Vsan> vsans ->
//                nxOsSwitchService.persistVsans(nxOsSwitch.refresh(), vsans)
//            }
//            // vCenter
//            vcenterVirtualMachines.each { Vcenter vcenter, Collection<VirtualMachine> virtualMachines ->
//                vcenterService.persistVirtualMachines(vcenter.refresh(), virtualMachines)
//            }
//            vcenterHosts.each { Vcenter vcenter, Collection<Host> hosts ->
//                vcenterService.persistHosts(vcenter.refresh(), hosts)
//            }
//            vcenterDisks.each { Vcenter vcenter, Collection<Disk> disks ->
//                vcenterService.persistDisks(vcenter.refresh(), disks)
//            }
//            vcenterService.manualConnectionManagement = false //restore and close conns
//            Vcenter.list().each { Vcenter vcenter ->
//                vcenterService.closeConnection(vcenter)
//            }
        } catch(Throwable t) {
            log.error "Error loading device data $t"
            if(retry) {
                log.info "Retry loading device data once more..."
                loadDeviceData(false)
            }
        }

    }

    @Transactional
    def loadDeviceDataSerially() {
        Ucs.list().each { Ucs ucs ->
            ucsService.getBlades(ucs)
            ucsService.getServers(ucs)
            ucsService.getVlans(ucs)
            ucsService.getVsans(ucs)
        }
        NxOsSwitch.list().each { NxOsSwitch nxOsSwitch ->
            nxOsSwitchService.getVsans(nxOsSwitch)
            nxOsSwitchService.getZones(nxOsSwitch)
        }
        Vcenter.list().each { Vcenter vcenter ->
            vcenterService.getVirtualMachines(vcenter)
            vcenterService.getHosts(vcenter)
            vcenterService.getDisks(vcenter)
        }
    }

    def createBootstrapData(boolean verifyConnection) {
        def jsonConfig = configurationService.getJsonConfig()

        if(Credential.list().size() == 0) {
            log.info "Create credential bootstrap data"
            jsonConfig.bootstrap.credentials.each { def jsonCredential ->
                Credential credential = new Credential()
                credential.properties = jsonCredential
                credential.save()
            }
        }

        if(Ucs.list().size() == 0) {
            log.info "Create UCS bootstrap data"
            jsonConfig.bootstrap.ucses.each { def jsonUcs ->
                Ucs ucs = new Ucs()
                ucs.properties = jsonUcs
                ucs.credential = Credential.findByName(jsonUcs.credential_ref)
                ucs.credential.addToCloudElements(ucs)
                ucs.connectionVerified = verifyConnection ? ucsService.verifyConnection(ucs) : true
                ucs.save()
            }
        }

        if(NxOsSwitch.list().size() == 0) {
            log.info "Create switch bootstrap data"
            jsonConfig.bootstrap.switches.each { def jsonSwitch ->
                NxOsSwitch nxOsSwitch = new NxOsSwitch()
                nxOsSwitch.properties = jsonSwitch
                nxOsSwitch.credential = Credential.findByName(jsonSwitch.credential_ref)
                nxOsSwitch.credential.addToCloudElements(nxOsSwitch)
                nxOsSwitch.connectionVerified = verifyConnection ? nxOsSwitchService.verifyConnection(nxOsSwitch) : true
                nxOsSwitch.save()
            }
        }

        if(Vcenter.list().size() == 0) {
            log.info "Create vCenter bootstrap data"
            jsonConfig.bootstrap.vcenters.each { def jsonVcenter ->
                Vcenter vcenter = new Vcenter()
                vcenter.properties = jsonVcenter
                vcenter.credential = Credential.findByName(jsonVcenter.credential_ref)
                vcenter.credential.addToCloudElements(vcenter)
                vcenter.connectionVerified = verifyConnection ? vcenterService.verifyConnection(vcenter) : true
                vcenter.save()
            }
        }
    }

    def getJsonConfig() {
        configurationService.getJsonConfig()
    }

    public SearchResult interpretSearchResults(Collection<Objects> results) {
        SearchResult searchResult = new SearchResult()

        searchResult.addBlades(results.findAll{it.class == Blade})
        searchResult.addServers(results.findAll{it.class == Server})
        searchResult.addVlans(results.findAll{it.class == Vlan})
        searchResult.addUcsVsans(results.findAll{it.class == Vsan && it.ucs != null})

        Set<Zone> zones = []
        zones.addAll(results.findAll{it.class == Zone})
        results.findAll{it.class == Zoneset}.each { Zoneset zoneset ->
            zones.addAll(zoneset.zones)
        }
        results.findAll{it.class == Port}.each { Port port ->
            zones << port.zone
        }
        searchResult.addZones(zones)
        searchResult.addNxOsSwitchVsans(results.findAll{it.class == Vsan && it.nxOsSwitch != null})

        searchResult.addVirtualMachines(results.findAll{it.class == VirtualMachine})
        searchResult.addHosts(results.findAll{it.class == Host})
        searchResult.addDisks(results.findAll{it.class == Disk})

        searchResult
    }

    public Collection<Object> search(Search search) {
        performSearch(search.value, search.ucs, search.nxOsSwitch, search.vcenter)
    }

    public Collection<Object> search(String searchValue) {
        performSearch(searchValue, true, true, true)
    }

    // domain objects
    public Collection<Object> performSearch(String searchValue, boolean ucs, boolean nxOsSwitch, boolean vcenter) {
        Set matches = [] // Use Set to avoid duplicates which will happen due to getFullyQualifiedName search as attribute!
        def allDomainObjectInstances = []
        if(ucs) {
            allDomainObjectInstances.addAll(Ucs.list())
            allDomainObjectInstances.addAll(Blade.list())
            allDomainObjectInstances.addAll(Server.list())
            allDomainObjectInstances.addAll(Vlan.list())
            allDomainObjectInstances.addAll(Vsan.findAllByUcsIsNotNull())
        }

        if(nxOsSwitch) {
            allDomainObjectInstances.addAll(NxOsSwitch.list())
            allDomainObjectInstances.addAll(Zone.list())
            allDomainObjectInstances.addAll(Zoneset.list())
            allDomainObjectInstances.addAll(Port.list())
            allDomainObjectInstances.addAll(Vsan.findAllByNxOsSwitchIsNotNull())
        }

        if(vcenter) {
            allDomainObjectInstances.addAll(Vcenter.list())
            allDomainObjectInstances.addAll(VirtualMachine.list())
            allDomainObjectInstances.addAll(Host.list())
            allDomainObjectInstances.addAll(Disk.list())
        }

        allDomainObjectInstances.each { def domainObject ->
            log.debug "Search domain object $domainObject"
            domainObject.properties.each { def key, def value ->
                if(value?.class == String) {
                    if(((String)value).toLowerCase().contains(searchValue.toLowerCase())) {
                        log.info("Domain object $domainObject id ${domainObject.id} property $key contains search value $searchValue")
                        matches << domainObject
                    }
                }
//                println "$key (${key.class} = $value (${value.class})"
            }
        }
        matches
    }
}
