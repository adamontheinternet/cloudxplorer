package com.cx.service

import grails.converters.JSON
import grails.transaction.Transactional
import groovy.text.Template
import groovy.text.XmlTemplateEngine


@Transactional
class ConfigurationService {

    private jsonObject

    // UCS
    private Map<String,Template> templates = [:]
    private String ucsUrl

    // NxOsSwitch
    private Map<String,String> commands = [:]
    private String allowedVersion

    public ConfigurationService() throws Exception {
        initialize()
    }

    private void initialize() throws Exception {
        try {
            log.info "Load Config.json"
            String json = this.class.classLoader.getResourceAsStream("Config.json")?.text?.trim()
            jsonObject = JSON.parse(json)
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

            // NxOsSwitch
            jsonObject.switch.commands.each { def jsonCommand ->
                String file = jsonCommand.file.trim()
                log.info "switch.command.file $file"
                String cmd = this.class.classLoader.getResourceAsStream("commands/$file").text.trim()
                commands[file] = cmd
                log.info "Loaded command $file $cmd"
            }
            allowedVersion = jsonConfig.switch.version
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

    public String getSwitchCommand(String commandName) {
        String command = commands[commandName]
        if(!command) throw new Exception("command.file invalid: $commandName")
        command
    }

    public String getSwitchVersion() {
        if(!allowedVersion || allowedVersion == "") throw new Exception("switch.verson invalid: $allowedVersion")
        else allowedVersion
    }
}
