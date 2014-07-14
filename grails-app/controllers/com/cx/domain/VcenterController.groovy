package com.cx.domain

import com.cx.service.VcenterService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class VcenterController {

    VcenterService vcenterService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Vcenter.list(params), model: [vcenterInstanceCount: Vcenter.count()]
    }

    def show(Vcenter vcenterInstance) {
        respond vcenterInstance
    }

    def create() {
        respond new Vcenter(params)
    }

    @Transactional
    def save(Vcenter vcenterInstance) {
        if (vcenterInstance == null) {
            notFound()
            return
        }

        if (vcenterInstance.hasErrors()) {
            respond vcenterInstance.errors, view: 'create'
            return
        }

        vcenterInstance.connectionVerified = vcenterService.verifyConnection(vcenterInstance)

        vcenterInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'vcenterInstance.label', default: 'Vcenter'), vcenterInstance.id])
                redirect vcenterInstance
            }
            '*' { respond vcenterInstance, [status: CREATED] }
        }
    }

    def edit(Vcenter vcenterInstance) {
        respond vcenterInstance
    }

    @Transactional
    def update(Vcenter vcenterInstance) {
        if (vcenterInstance == null) {
            notFound()
            return
        }

        if (vcenterInstance.hasErrors()) {
            respond vcenterInstance.errors, view: 'edit'
            return
        }

        vcenterInstance.connectionVerified = vcenterService.verifyConnection(vcenterInstance)

        vcenterInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Vcenter.label', default: 'Vcenter'), vcenterInstance.id])
                redirect vcenterInstance
            }
            '*' { respond vcenterInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Vcenter vcenterInstance) {

        if (vcenterInstance == null) {
            notFound()
            return
        }

        vcenterInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Vcenter.label', default: 'Vcenter'), vcenterInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'vcenterInstance.label', default: 'Vcenter'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    @Transactional
    def virtualMachines(Vcenter vcenterInstance) {
        def returnMap = [vcenterInstance:vcenterInstance]
        try {
            log.info "Find virtual machines for ${vcenterInstance.ip}"
            if(vcenterInstance.connectionVerified) {
                Collection<VirtualMachine> virtualMachines = vcenterService.getVirtualMachines(vcenterInstance)
                returnMap["virtualMachines"] = virtualMachines
            } else {
                log.info "Connection not verified for vCenter ${vcenterInstance.ip}"
                returnMap["error"] = "Connection not verified for vCenter ${vcenterInstance.ip}"
            }
        } catch(Exception e) {
            log.error "virtualMachines error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }

    @Transactional
    def hosts(Vcenter vcenterInstance) {
        def returnMap = [vcenterInstance:vcenterInstance]
        try {
            log.info "Find hosts for ${vcenterInstance.ip}"
            if(vcenterInstance.connectionVerified) {
                Collection<Host> hosts = vcenterService.getHosts(vcenterInstance)
                returnMap["hosts"] = hosts
            } else {
                log.info "Connection not verified for vCenter ${vcenterInstance.ip}"
                returnMap["error"] = "Connection not verified for vCenter ${vcenterInstance.ip}"
            }
        } catch(Exception e) {
            log.error "hosts error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }

    @Transactional
    def disks(Vcenter vcenterInstance) {
        def returnMap = [vcenterInstance:vcenterInstance]
        try {
            log.info "Find disks for ${vcenterInstance.ip}"
            if(vcenterInstance.connectionVerified) {
                Collection<Disk> disks = vcenterService.getDisks(vcenterInstance)
                returnMap["disks"] = disks
            } else {
                log.info "Connection not verified for vCenter ${vcenterInstance.ip}"
                returnMap["error"] = "Connection not verified for vCenter ${vcenterInstance.ip}"
            }
        } catch(Exception e) {
            log.error "disks error $e"
            returnMap["error"] = e.message
        }
        returnMap
    }
}