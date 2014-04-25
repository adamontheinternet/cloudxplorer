package com.cx.domain

import com.cx.service.NxOsSwitchService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NxOsSwitchController {

    NxOsSwitchService nxOsSwitchService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond NxOsSwitch.list(params), model:[nxOsSwitchInstanceCount: NxOsSwitch.count()]
    }

    def show(NxOsSwitch nxOsSwitchInstance) {
        respond nxOsSwitchInstance
    }

    def create() {
        respond new NxOsSwitch(params)
    }

    @Transactional
    def save(NxOsSwitch nxOsSwitchInstance) {
        if (nxOsSwitchInstance == null) {
            notFound()
            return
        }

        if (nxOsSwitchInstance.hasErrors()) {
            respond nxOsSwitchInstance.errors, view:'create'
            return
        }

        nxOsSwitchInstance.connectionVerified = nxOsSwitchService.verifyConnection(nxOsSwitchInstance)

        nxOsSwitchInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nxOsSwitchInstance.label', default: 'NxOsSwitch'), nxOsSwitchInstance.id])
                redirect nxOsSwitchInstance
            }
            '*' { respond nxOsSwitchInstance, [status: CREATED] }
        }
    }

    def edit(NxOsSwitch nxOsSwitchInstance) {
        respond nxOsSwitchInstance
    }

    @Transactional
    def update(NxOsSwitch nxOsSwitchInstance) {
        if (nxOsSwitchInstance == null) {
            notFound()
            return
        }

        if (nxOsSwitchInstance.hasErrors()) {
            respond nxOsSwitchInstance.errors, view:'edit'
            return
        }

        nxOsSwitchInstance.connectionVerified = nxOsSwitchService.verifyConnection(nxOsSwitchInstance)

        nxOsSwitchInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'NxOsSwitch.label', default: 'NxOsSwitch'), nxOsSwitchInstance.id])
                redirect nxOsSwitchInstance
            }
            '*'{ respond nxOsSwitchInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(NxOsSwitch nxOsSwitchInstance) {

        if (nxOsSwitchInstance == null) {
            notFound()
            return
        }

        nxOsSwitchInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'NxOsSwitch.label', default: 'NxOsSwitch'), nxOsSwitchInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nxOsSwitchInstance.label', default: 'NxOsSwitch'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def vsans(NxOsSwitch nxOsSwitchInstance) {
        try {
            if(nxOsSwitchInstance.connectionVerified) {
                Collection<Vsan> vsans = nxOsSwitchService.getVsans(nxOsSwitchInstance)
                [vsans:vsans, nxOsSwitchInstance:nxOsSwitchInstance]
            } else {
                log.info "Connection not verified for $nxOsSwitchInstance"
                [error:"Connection not verified for $nxOsSwitchInstance"]
            }

        } catch(Exception e) {
            log.error "vsans error $e"
            [error:e.message]
        }

    }

    @Transactional
    def zones(NxOsSwitch nxOsSwitchInstance) {
        try {
            if(nxOsSwitchInstance.connectionVerified) {
                Collection<Zoneset> zonesets = nxOsSwitchService.getZones(nxOsSwitchInstance)
                [zonesets:zonesets, nxOsSwitchInstance:nxOsSwitchInstance]
            } else {
                log.info "Connection not verified for $nxOsSwitchInstance"
                [error:"Connection not verified for switch $nxOsSwitchInstance"]
            }

        } catch(Exception e) {
            log.error "zones error $e"
            [error:e.message]
        }
    }
}
