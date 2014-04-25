package com.cx.domain

import com.cx.service.UcsService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UcsController {

    UcsService ucsService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Ucs.list(params), model:[ucsInstanceCount: Ucs.count()]
    }

    def show(Ucs ucsInstance) {
        respond ucsInstance
    }

    def create() {
        respond new Ucs(params)
    }

    @Transactional
    def save(Ucs ucsInstance) {
        if (ucsInstance == null) {
            notFound()
            return
        }

        if (ucsInstance.hasErrors()) {
            respond ucsInstance.errors, view:'create'
            return
        }

        ucsInstance.connectionVerified = ucsService.verifyConnection(ucsInstance)

        ucsInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ucsInstance.label', default: 'Ucs'), ucsInstance.id])
                redirect ucsInstance
            }
            '*' { respond ucsInstance, [status: CREATED] }
        }
    }

    def edit(Ucs ucsInstance) {
        respond ucsInstance
    }

    @Transactional
    def update(Ucs ucsInstance) {
        if (ucsInstance == null) {
            notFound()
            return
        }

        if (ucsInstance.hasErrors()) {
            respond ucsInstance.errors, view:'edit'
            return
        }

        ucsInstance.connectionVerified = ucsService.verifyConnection(ucsInstance)

        ucsInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Ucs.label', default: 'Ucs'), ucsInstance.id])
                redirect ucsInstance
            }
            '*'{ respond ucsInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Ucs ucsInstance) {

        if (ucsInstance == null) {
            notFound()
            return
        }

        ucsInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Ucs.label', default: 'Ucs'), ucsInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ucsInstance.label', default: 'Ucs'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def blades(Ucs ucsInstance) {
        def returnMap = [ucsInstance:ucsInstance]
        try {
            log.info "Find blades for ${ucsInstance.ip}"
            if(ucsInstance.connectionVerified) {
                Collection<Blade> blades = ucsService.getBlades(ucsInstance)
                returnMap["blades"] = blades.sort{it.dn}
            } else {
                log.info "Connection not verified for UCS ${ucsInstance.ip}"
                returnMap["error"] = "Connection not verified for UCS ${ucsInstance.ip}"
            }
        } catch(Exception e) {
            log.error "blades error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }

    @Transactional
    def vlans(Ucs ucsInstance) {
        def returnMap = [ucsInstance:ucsInstance]
        try {
            log.info "Find vlans for ${ucsInstance.ip}"
            if(ucsInstance.connectionVerified) {
                Collection<Vlan> vlans = ucsService.getVlans(ucsInstance)
                returnMap["vlans"] = vlans.sort{it.networkId}
            } else {
                log.info "Connection not verified for UCS ${ucsInstance.ip}"
                returnMap["error"] = "Connection not verified for UCS ${ucsInstance.ip}"
            }
        } catch(Exception e) {
            log.error "vlans error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }

    @Transactional
    def vsans(Ucs ucsInstance) {
        def returnMap = [ucsInstance:ucsInstance]
        try {
            log.info "Find vsans for ${ucsInstance.ip}"
            if(ucsInstance.connectionVerified) {
                Collection<Map> vsans = ucsService.getVsans(ucsInstance)
                returnMap["vsans"] = vsans.sort{it.networkId}
            } else {
                log.info "Connection not verified for UCS ${ucsInstance.ip}"
                returnMap["error"] = "Connection not verified for UCS ${ucsInstance.ip}"
            }
        } catch(Exception e) {
            log.error "vsans error $e"
            returnMap["error"] = e.message
        }
        returnMap

    }

    @Transactional
    def servers(Ucs ucsInstance) {
        def returnMap = [ucsInstance:ucsInstance]
        try {
            log.info "Find servers for ${ucsInstance.ip}"
            if(ucsInstance.connectionVerified) {
                Collection<Server> servers = ucsService.getServers(ucsInstance)
                returnMap["servers"] = servers.sort{it.dn}
            } else {
                log.info "Connection not verified for UCS ${ucsInstance.ip}"
                returnMap["error"] = "Connection not verified for UCS ${ucsInstance.ip}"
            }
        } catch(Exception e) {
            log.error "servers error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }
}
