package com.cx.service

import com.cx.domain.Blade
import com.cx.domain.Server
import com.cx.domain.Ucs
import com.cx.domain.Vlan
import com.cx.domain.Vsan
import grails.transaction.Transactional
import groovy.text.Template
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.XML

@Transactional
class UcsService {
    ConfigurationService configurationService
    UcsResponseInterpreterService ucsResponseInterpreterService

    private Map<String,String> ucsSessionMap = [:]
    private Map<String,RESTClient> ucsClientMap = [:]


    /*
    Infrastructure
     */
    private RESTClient createOrGetRestClient(Ucs ucs) throws Exception {
        if(!ucsClientMap[ucs.ip]) {
            ucsClientMap[ucs.ip] = new RESTClient(getUrl(ucs.ip))
        }
        ucsClientMap[ucs.ip]
    }

    private String createOrGetSession(Ucs ucs) throws Exception {
        if(!ucsSessionMap[ucs.ip]) {
            ucsSessionMap[ucs.ip] = createSession(ucs)
        }
        ucsSessionMap[ucs.ip]
    }

    private void destroySession(Ucs ucs) {
        try {
            logout(ucs)
        } catch(Exception e) {
            log.info "Ignore error closing session to ${ucs.ip} $e"
        } finally {
            ucsSessionMap[ucs.ip] = null
            ucsClientMap[ucs.ip] = null
        }
    }

    private String createSession(Ucs ucs) throws Exception {
        String cookie = login(ucs)
        log.info "Session $cookie created for UCS ${ucs.ip}"
        cookie
    }

    private Template getTemplate(String templateName) {
        configurationService.getUcsTemplate(templateName)
    }

    private String getUrl(String ip) {
        configurationService.getUcsUrl().replace("\$ip", ip)
    }


    /*
    Login and Logout
     */
    private String login(Ucs ucs) throws Exception {
        RESTClient restClient = createOrGetRestClient(ucs)

        String body = getTemplate("Login.xml").make([username: ucs.credential.username, password: ucs.credential.password]).toString()
        def response = restClient.post(contentType: XML, requestContentType: XML, body: body)
        if(response.status == 200) return response.data.@outCookie
        else throw new Exception("Session could not be created")
    }

    private void logout(Ucs ucs) throws Exception {
        String cookie = ucsSessionMap[ucs.ip]
        if(!cookie) {
            log.info "No session cookie for UCS ${ucs.ip} - No logout required"
            return
        }
        RESTClient restClient = createOrGetRestClient(ucs)

        String body = getTemplate("Logout.xml").make([cookie: cookie]).toString()
        def response = restClient.post(contentType: XML,requestContentType: XML, body: body)
        if(response.status == 200) log.info "Session $cookie destroyed"
        else throw new Exception("Session $cookie could not be destroyed")
    }


    /*
    Data finders and public accessible services
     */
    public boolean verifyConnection(Ucs ucs) {
        try {
            destroySession(ucs)
            String cookie = createOrGetSession(ucs)
            if(!cookie) throw new Exception("UCS ${ucs.ip} session cookie is null")
            true
        } catch(Exception e) {
            log.error("Connection to UCS ${ucs.ip} failed $e")
            false
        } finally {
            destroySession(ucs)
        }
    }

    public Collection<Blade> getBlades(Ucs ucs) throws Exception {
        try {
            String cookie =  createOrGetSession(ucs)
            RESTClient restClient = createOrGetRestClient(ucs)

            String body = getTemplate("Blades.xml").make([cookie: cookie]).toString()
            def response = restClient.post(contentType: XML, requestContentType: XML, body: body)

            Collection<Blade> blades = ucsResponseInterpreterService.interpretBladesResponse(response)
            ucs.blades*.delete()
            ucs.blades.clear()
            blades.each { Blade blade ->
                ucs.addToBlades(blade)
                blade.ucs = ucs
                blade.save()
            }
            ucs.save()
            ucs.blades
        } catch(Exception e) {
            log.error "Error getting blades for ucs ${ucs.ip} $e"
            throw e
        } finally  {
            destroySession(ucs) // Inefficient
        }

    }

    Collection<Vlan> getVlans(Ucs ucs) {
        try {
            String cookie = createOrGetSession(ucs)
            RESTClient restClient = createOrGetRestClient(ucs)

            String body = getTemplate("Vlans.xml").make([cookie: cookie]).toString()
            def response = restClient.post(contentType: XML,requestContentType: XML,body: body)

            Collection<Vlan> vlans = ucsResponseInterpreterService.interpretVlansResponse(response)
            ucs.vlans*.delete()
            ucs.vlans.clear()
            vlans.each { Vlan vlan ->
                ucs.addToVlans(vlan)
                vlan.ucs = ucs
                vlan.save()
            }
            ucs.save()
            ucs.vlans
        } catch(Exception e) {
            log.error "Error getting vlans for ucs ${ucs.ip} $e"
            throw e
        } finally  {
            destroySession(ucs) // Inefficient
        }
    }

    Collection<Map> getVsans(Ucs ucs) {
        try {
            String cookie = createOrGetSession(ucs)
            RESTClient restClient = createOrGetRestClient(ucs)

            String body = getTemplate("Vsans.xml").make([cookie: cookie]).toString()
            def response = restClient.post(contentType: XML,requestContentType: XML,body: body)

            Collection<Vsan> vsans = ucsResponseInterpreterService.interpretVsansResponse(response)
            ucs.vsans*.delete()
            ucs.vsans.clear()
            vsans.each { Vsan vsan ->
                ucs.addToVsans(vsan)
                vsan.ucs = ucs
                vsan.save()
            }
            ucs.save()
            ucs.vsans
        } catch(Exception e) {
            log.error "Error getting vsans for ucs ${ucs.ip} $e"
            throw e
        } finally  {
            destroySession(ucs) // Inefficient
        }
    }

    Collection<Server> getServers(Ucs ucs) {
        try {
            String cookie = createOrGetSession(ucs)
            RESTClient restClient = createOrGetRestClient(ucs)

            String body = getTemplate("Servers.xml").make([cookie: cookie]).toString()
            def response = restClient.post(contentType: XML,requestContentType: XML,body: body)

            Collection<Server> servers = ucsResponseInterpreterService.interpretServersResponse(response)
            ucs.servers*.delete()
            ucs.servers.clear()
            servers.each { Server server ->
                ucs.addToServers(server)
                server.ucs = ucs
                server.save()
            }
            ucs.save()
            ucs.servers
        } catch(Exception e) {
            log.error "Error getting servers for ucs ${ucs.ip} $e"
            throw e
        } finally  {
            destroySession(ucs) // Inefficient
        }
    }
}
