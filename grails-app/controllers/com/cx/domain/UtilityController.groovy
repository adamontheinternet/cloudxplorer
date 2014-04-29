package com.cx.domain

import com.cx.service.UtilityService
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UtilityController {
    UtilityService utilityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        String options = "Try some of the following: </br><ul>"
        options += "<li>bootstrap - Create bootstrap data</li>"
        options += "<li>load - Load all device data</li>"
        options += "<li>config - Show system JSON config</li>"
        options += "<li>search?search=value - Search device data</li>"
        render options + "</ul>"
    }

    def bootstrap() {
        utilityService.createBootstrapData()
        render "Bootstrap data created"
    }

    def config() {
        render utilityService.getJsonConfig()
    }

    def search() {
        String searchParam = params.search
        log.info "Search for $searchParam"
        def objects =  utilityService.search(searchParam)
        String output = "<ul>"
        objects.each {
            output += "<li>${it}</li>"
        }
        render output + "</ul>"
    }

    def load() {
        utilityService.loadDeviceData()
        render "Device data loaded"
    }
}
