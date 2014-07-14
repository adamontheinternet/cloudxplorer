package com.cx.domain

import com.cx.service.UtilityService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SearchController {

    UtilityService utilityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Search.list(params), model:[searchInstanceCount: Search.count()]
    }
//
//    def show(Search searchInstance) {
//        respond searchInstance
//    }
//
//    def create() {
//        respond new Search(params)
//    }
//
//    @Transactional
//    def save(Search searchInstance) {
//        if (searchInstance == null) {
//            notFound()
//            return
//        }
//
//        if (searchInstance.hasErrors()) {
//            respond searchInstance.errors, view:'create'
//            return
//        }
//
//        searchInstance.save flush:true
//
//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.created.message', args: [message(code: 'searchInstance.label', default: 'Search'), searchInstance.id])
//                redirect searchInstance
//            }
//            '*' { respond searchInstance, [status: CREATED] }
//        }
//    }
//
//    def edit(Search searchInstance) {
//        respond searchInstance
//    }
//
//    @Transactional
//    def update(Search searchInstance) {
//        if (searchInstance == null) {
//            notFound()
//            return
//        }
//
//        if (searchInstance.hasErrors()) {
//            respond searchInstance.errors, view:'edit'
//            return
//        }
//
//        searchInstance.save flush:true
//
//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.updated.message', args: [message(code: 'Search.label', default: 'Search'), searchInstance.id])
//                redirect searchInstance
//            }
//            '*'{ respond searchInstance, [status: OK] }
//        }
//    }
//
//    @Transactional
//    def delete(Search searchInstance) {
//
//        if (searchInstance == null) {
//            notFound()
//            return
//        }
//
//        searchInstance.delete flush:true
//
//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Search.label', default: 'Search'), searchInstance.id])
//                redirect action:"index", method:"GET"
//            }
//            '*'{ render status: NO_CONTENT }
//        }
//    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchInstance.label', default: 'Search'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def load(Search searchInstance) {
        long start = System.currentTimeMillis()
        utilityService.loadDeviceData()
        long end = System.currentTimeMillis()
        long timeElapsed = ((end - start) / 1000)

        Integer deviceCount = CloudElement.list().size()
        flash.message =  "Data from $deviceCount devices loaded in $timeElapsed seconds."
        redirect(action:"index")
        return
    }

    @Transactional
    def search(Search searchInstance) {
        if (searchInstance == null) {
            notFound()
            return
        }

        if(searchInstance.value == null || searchInstance.value.trim() == "") {
//            return [searchInstance:searchInstance, error:"Please enter a search value."]
//            log.info "return"

            flash.error =  "Please enter a search value."
            redirect(action:"index")
            return
        }

        boolean domainSelected = searchInstance?.ucs || searchInstance?.nxOsSwitch || searchInstance?.vcenter
        if(!domainSelected) {
            flash.error =  "Please select a search domain."
            redirect(action:"index")
            return

//            return [searchInstance:searchInstance, error:"Please select a search domain."]
        }

        def objects = utilityService.search(searchInstance)
//        Map<String,List<Object>> partitionedResults = utilityService.partitionSearchResults(objects)
//        partitionedResults["searchInstance"] = searchInstance
//        partitionedResults

//        [objects:objects.sort{it.getType()}, searchInstance:searchInstance]

        SearchResult searchResult = utilityService.interpretSearchResults(objects)
        searchResult.getZones().each { def key, value ->
            log.info "Switch ${key} zones ${value.collect{it.id}}"
        }
        [searchResult:searchResult, searchInstance:searchInstance]
    }
}
