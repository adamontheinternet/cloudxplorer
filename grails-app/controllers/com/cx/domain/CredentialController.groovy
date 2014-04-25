package com.cx.domain

import com.cx.service.NxOsSwitchService
import com.cx.service.UcsService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CredentialController {

    UcsService ucsService
    NxOsSwitchService nxOsSwitchService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Credential.list(params), model:[credentialInstanceCount: Credential.count()]
    }

    def show(Credential credentialInstance) {
        respond credentialInstance
    }

    def create() {
        respond new Credential(params)
    }

    private verifyConnections(Credential credentialInstance) {
        // Refactor to factory pattern
        credentialInstance.cloudElements.each { CloudElement cloudElement ->
            if(cloudElement.getType() == "UCS") cloudElement.connectionVerified = ucsService.verifyConnection(cloudElement)
            else if(cloudElement.getType() == "NX-OS Switch") cloudElement.connectionVerified = nxOsSwitchService.verifyConnection(cloudElement)
            else log.info "Unknown cloud element ${cloudElement.type}"
            cloudElement.save()
        }
    }

    @Transactional
    def save(Credential credentialInstance) {
        if (credentialInstance == null) {
            notFound()
            return
        }

        if (credentialInstance.hasErrors()) {
            respond credentialInstance.errors, view:'create'
            return
        }

        verifyConnections(credentialInstance)

        credentialInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'credentialInstance.label', default: 'Credential'), credentialInstance.id])
                redirect credentialInstance
            }
            '*' { respond credentialInstance, [status: CREATED] }
        }
    }

    def edit(Credential credentialInstance) {
        respond credentialInstance
    }

    @Transactional
    def update(Credential credentialInstance) {
        if (credentialInstance == null) {
            notFound()
            return
        }

        if (credentialInstance.hasErrors()) {
            respond credentialInstance.errors, view:'edit'
            return
        }

        verifyConnections(credentialInstance)

        credentialInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Credential.label', default: 'Credential'), credentialInstance.id])
                redirect credentialInstance
            }
            '*'{ respond credentialInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Credential credentialInstance) {

        if (credentialInstance == null) {
            notFound()
            return
        }

        credentialInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Credential.label', default: 'Credential'), credentialInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'credentialInstance.label', default: 'Credential'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
