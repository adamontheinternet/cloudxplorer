package com.cx.service

import com.cx.domain.Blade
import com.cx.domain.Credential
import com.cx.domain.NxOsSwitch
import com.cx.domain.Port
import com.cx.domain.Search
import com.cx.domain.Server
import com.cx.domain.Ucs
import com.cx.domain.Vcenter
import com.cx.domain.Vlan
import com.cx.domain.Vsan
import com.cx.domain.Zone
import com.cx.domain.Zoneset
import grails.transaction.Transactional

@Transactional
class UtilityService {
    ConfigurationService configurationService
    UcsService ucsService
    NxOsSwitchService nxOsSwitchService
    VcenterService vcenterService

    def createBootstrapData() {
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
//                ucs.connectionVerified = ucsService.verifyConnection(ucs)
                ucs.connectionVerified =true
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
//                nxOsSwitch.connectionVerified = nxOsSwitchService.verifyConnection(nxOsSwitch)
                nxOsSwitch.connectionVerified = true
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
//                vcenter.connectionVerified = vcenterService.verifyConnection(vcenter)
                vcenter.connectionVerified = true
                vcenter.save()
            }
        }
    }

    def loadDeviceData() {
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
    }

    def getJsonConfig() {
        configurationService.getJsonConfig()
    }

    public Map<String,List<Object>> partitionSearchResults(Collection<Objects> results) {
        def partitionedResults = [:]
        partitionedResults["ucses"] = results.findAll{it.class == Ucs}
        partitionedResults["blades"] = results.findAll{it.class == Blade}
        partitionedResults["servers"] = results.findAll{it.class == Server}
        partitionedResults["vlans"] = results.findAll{it.class == Vlan}
        partitionedResults["ucsVsans"] = results.findAll{it.class == Vsan && it.ucs != null}

        partitionedResults["nxOsSwitches"] = results.findAll{it.class == NxOsSwitch}
        partitionedResults["zonesets"] = results.findAll{it.class == Zoneset}
        partitionedResults["zones"] = results.findAll{it.class == Zone}
        partitionedResults["ports"] = results.findAll{it.class == Port}
        partitionedResults["nxOsSwitchVsans"] = results.findAll{it.class == Vsan && it.nxOsSwitch != null}
        partitionedResults
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
