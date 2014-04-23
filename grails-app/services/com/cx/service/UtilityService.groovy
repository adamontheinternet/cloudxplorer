package com.cx.service

import com.cx.domain.Credential
import com.cx.domain.Ucs
import grails.transaction.Transactional

@Transactional
class UtilityService {
    ConfigurationService configurationService
    UcsService ucsService

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
    }

    def getJsonConfig() {
        configurationService.getJsonConfig()
    }
}
