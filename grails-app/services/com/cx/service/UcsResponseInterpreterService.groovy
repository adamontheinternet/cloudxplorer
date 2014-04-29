package com.cx.service

import com.cx.domain.Blade
import com.cx.domain.Server
import com.cx.domain.Vlan
import com.cx.domain.Vsan
import grails.transaction.Transactional

@Transactional
class UcsResponseInterpreterService {

    public Collection<Blade> interpretBladesResponse(def response) {
        Collection<Map> blades = []
        response.data.outConfigs.computeBlade.each { def blade ->
            blades << [dn: blade.@dn.text(), assignedToDn: blade.@assignedToDn.text()]
        }
        convertMapsToBlades(blades)
    }

    // Unsaved instances
    private Collection<Blade> convertMapsToBlades(Collection<Map> bladeMaps) {
        Collection<Blade> blades = []
        bladeMaps.each { Map bladeMap ->
            Blade blade = new Blade()
            blade.dn = bladeMap["dn"]
            blade.assignedTo = bladeMap["assignedToDn"]
            blades << blade
        }
        blades
    }

    public Collection<Vlan> interpretVsansResponse(def response) {
        Collection<Map> vsans = []
        response.data.outConfigs.fabricVsan.each { def vsan ->
            vsans << [dn:vsan.@dn.text(), name:vsan.@name.text(), switchId:vsan.@switchId.text(), networkId:vsan.@id.text()]
        }
        convertMapsToVsans(vsans)
    }

    // Unsaved instances
    private Collection<Vsan> convertMapsToVsans(Collection<Map> vsanMaps) {
        Collection<Vsan> vsans = []
        vsanMaps.each { Map vsanMap ->
            Vsan vsan = new Vsan()
            vsan.name = vsanMap["name"]
            vsan.vsan = vsanMap["networkId"]
            vsan.dn = vsanMap["dn"]
            vsan.switchId = vsanMap["switchId"]
            vsans << vsan
        }
        vsans
    }

    public Collection<Vlan> interpretVlansResponse(def response) {
        Collection<Map> vlans = []
        response.data.outConfigs.fabricVlan.each { def vlan ->
            vlans << [dn: vlan.@dn.text(), id: vlan.@id.text() as Integer, name: vlan.@name.text()]
        }
        convertMapsToVlans(vlans)
    }

    // Unsaved instances
    private Collection<Vlan> convertMapsToVlans(Collection<Map> vlanMaps) {
        Collection<Vlan> vlans = []
        vlanMaps.each { Map vlanMap ->
            Vlan vlan = new Vlan()
            vlan.networkId = vlanMap["id"]
            vlan.dn = vlanMap["dn"]
            vlan.name = vlanMap["name"]
            vlans << vlan
        }
        vlans
    }

    public Collection<Server> interpretServersResponse(def response) {
        Collection<Map> servers = []
        response.data.outConfigs.lsServer.each { def server ->
            servers << [dn:server.@dn.text(), assignState:server.@assignState.text(), configState:server.@configState.text(),
                    operState:server.@operState.text(), assocState:server.@assocState.text()]
        }
        convertMapsToServers(servers)
    }

    // Unsaved instances
    private Collection<Server> convertMapsToServers(Collection<Map> serverMaps) {
        Collection<Server> servers = []
        serverMaps.each { Map serverMap ->
            Server server = new Server()
            server.dn = serverMap["dn"]
            server.assignState = serverMap["assignState"]
            server.configState = serverMap["configState"]
            server.operState = serverMap["operState"]
            server.assocState = serverMap["assocState"]
            servers << server
        }
        servers
    }
}
