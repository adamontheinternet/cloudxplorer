package com.cx.domain

/**
 * Created with IntelliJ IDEA.
 * User: alaplante
 * Date: 5/2/14
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
class SearchResult {

    private Map<Ucs, Collection<Blade>> ucsBlades = [:]
    private Map<Ucs, Collection<Server>> ucsServers = [:]
    private Map<Ucs, Collection<Vlan>> ucsVlans = [:]
    private Map<Ucs, Collection<Vsan>> ucsVsans = [:]

    private Map<NxOsSwitch, Collection<Zone>> nxOsSwitchZones = [:]
    private Map<NxOsSwitch, Collection<Vsan>> nxOsSwitchVsans = [:]


    public void addBlades(Collection<Blade> blades) {
        blades.each { Blade blade ->
            Ucs ucs = blade.ucs
            if(ucsBlades[ucs] == null) {
                ucsBlades[ucs] = []
            }
            ucsBlades[ucs] << blade
        }
    }

    public Map<Ucs, Collection<Blade>> getBlades() {
        ucsBlades
    }

    public void addServers(Collection<Server> servers) {
        servers.each { Server server ->
            Ucs ucs = server.ucs
            if(ucsServers[ucs] == null) {
                ucsServers[ucs] = []
            }
            ucsServers[ucs] << server
        }
    }

    public Map<Ucs, Collection<Server>> getServers() {
        ucsServers
    }

    public void addVlans(Collection<Vlan> vlans) {
        vlans.each { Vlan vlan ->
            Ucs ucs = vlan.ucs
            if(ucsVlans[ucs] == null) {
                ucsVlans[ucs] = []
            }
            ucsVlans[ucs] << vlan
        }
    }

    public Map<Ucs, Collection<Vlan>> getVlans() {
        ucsVlans
    }

    public void addUcsVsans(Collection<Vsan> vsans) {
        vsans.each { Vsan vsan ->
            Ucs ucs = vsan.ucs
            if(ucsVsans[ucs] == null) {
                ucsVsans[ucs] = []
            }
            ucsVsans[ucs] << vsan
        }
    }

    public Map<Ucs, Collection<Vsan>> getUcsVsans() {
        ucsVsans
    }

    public void addZones(Collection<Zone> zones) {
        zones.each { Zone zone ->
            NxOsSwitch nxOsSwitch = zone.zoneset.nxOsSwitch
            if(nxOsSwitchZones[nxOsSwitch] == null) {
                nxOsSwitchZones[nxOsSwitch] = []
            }
            nxOsSwitchZones[nxOsSwitch] << zone
        }
    }

    public Map<NxOsSwitch, Collection<Zone>> getZones() {
        nxOsSwitchZones
    }

    public void addNxOsSwitchVsans(Collection<Vsan> vsans) {
        vsans.each { Vsan vsan ->
            NxOsSwitch nxOsSwitch = vsan.nxOsSwitch
            if(nxOsSwitchVsans[nxOsSwitch] == null) {
                nxOsSwitchVsans[nxOsSwitch] = []
            }
            nxOsSwitchVsans[nxOsSwitch] << vsan
        }
    }

    public Map<NxOsSwitch, Collection<Vsan>> getNxOsSwitchVsans() {
        nxOsSwitchVsans
    }
}
