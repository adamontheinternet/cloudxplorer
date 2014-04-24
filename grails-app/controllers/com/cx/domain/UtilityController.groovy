package com.cx.domain

import com.cx.service.UtilityService
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UtilityController {
    UtilityService utilityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        render "try adding 'bootstrap' to create data or 'config' to show configuration"
    }

    def bootstrap() {
        utilityService.createBootstrapData()
        render "Bootstrap data created"
    }

    def config() {
        render utilityService.getJsonConfig()
    }
}
