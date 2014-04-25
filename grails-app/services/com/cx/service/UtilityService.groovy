package com.cx.service

import com.cx.domain.Credential
import com.cx.domain.NxOsSwitch
import com.cx.domain.Ucs
import grails.transaction.Transactional

@Transactional
class UtilityService {
    ConfigurationService configurationService
    UcsService ucsService
    NxOsSwitchService nxOsSwitchService

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
                ucs.connectionVerified = ucsService.verifyConnection(ucs)
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
                nxOsSwitch.connectionVerified = nxOsSwitchService.verifyConnection(nxOsSwitch)
                nxOsSwitch.save()
            }
        }
    }

    def getJsonConfig() {
        configurationService.getJsonConfig()
    }
}
