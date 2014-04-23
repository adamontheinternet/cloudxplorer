package com.cx.service

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.text.Template
import groovy.text.XmlTemplateEngine


@Transactional
class ConfigurationService {
    private jsonObject
    // UCS
    private Map<String,Template> templates = [:]
    private String ucsUrl

    public ConfigurationService() throws Exception {
        initialize()
    }

    private void initialize() throws Exception {
        try {
            log.info "Load Config.json"
            String json = this.class.classLoader.getResourceAsStream("Config.json")?.text?.trim()
            jsonObject = new JsonSlurper().parseText(json)
            log.info "System config parsed $jsonObject"

            // UCS
            ucsUrl = jsonObject.ucs.url.trim()

            XmlTemplateEngine xmlTemplateEngine = new XmlTemplateEngine()
            jsonObject.ucs.templates.each { def jsonTemplate ->
                String file = jsonTemplate.file.trim()
                log.info "ucs.template.file $file"
                String templateXmlContents = this.class.classLoader.getResourceAsStream("templates/$file").text.trim()
                Template template = xmlTemplateEngine.createTemplate(templateXmlContents)
                templates[file] = template
                log.info "Loaded template $file $templateXmlContents"
            }
        } catch(Exception e) {
            log.error "Exception loading Config.json $e"
            throw e
        }
    }

    public Object getJsonConfig() {
        jsonObject
    }

    public String getUcsUrl() {
        if(!ucsUrl || ucsUrl == "") throw new Exception("ucs.url invalid: $ucsUrl")
        else ucsUrl
    }

    public Template getUcsTemplate(String templateName) {
        Template template = templates[templateName]
        if(!template) throw new Exception("template.file invalid: $templateName")
        template
    }
}
