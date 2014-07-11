package com.cx.service

import com.cx.domain.NxOsSwitch
import com.cx.domain.Vsan
import com.cx.domain.Zoneset
import grails.transaction.Transactional

@Transactional
class NxOsSwitchService {

    ConfigurationService configurationService
    SshService sshService
    NxOsSwitchResponseInterpreterService nxOsSwitchResponseInterpreterService

    /*
    Infrastructure
     */
    private String getCommand(String commandName) {
        configurationService.getSwitchCommand(commandName)
    }

    private Boolean isVersionSupported(String version) {
        configurationService.getSwitchVersion() == version
    }

    /*
    Data finders and public accessible services
     */
    public boolean verifyConnection(NxOsSwitch nxOsSwitch) {
        String command = getCommand("Version.txt")
        String commandResult = sshService.runCommand(nxOsSwitch.ip, nxOsSwitch.credential.username, nxOsSwitch.credential.password, command)
        String firstLine = nxOsSwitchResponseInterpreterService.interpretVersionResponse(commandResult)
        isVersionSupported(firstLine)
    }

    public void persistVsans(NxOsSwitch nxOsSwitch, Collection<Vsan> vsans) {
        log.info "Persist ${vsans.size()} vsans for NX-OS switch $nxOsSwitch"
        nxOsSwitch.vsans*.delete()
        nxOsSwitch.vsans.clear()
        vsans.each { Vsan vsan ->
            nxOsSwitch.addToVsans(vsan)
            vsan.nxOsSwitch = nxOsSwitch
            vsan.save()
        }
        nxOsSwitch.save()
    }

    public Collection<Vsan> getVsans(NxOsSwitch nxOsSwitch, boolean persist = true) {
        String command = getCommand("Vsans.txt")
        String commandResult = sshService.runCommand(nxOsSwitch.ip, nxOsSwitch.credential.username, nxOsSwitch.credential.password, command)
        Collection<Vsan> vsans = nxOsSwitchResponseInterpreterService.interpretVsansResponse(commandResult)
        if(persist) {
            persistVsans(nxOsSwitch, vsans)
            nxOsSwitch.vsans
        } else {
            vsans
        }
    }

    public void persistZones(NxOsSwitch nxOsSwitch, Collection<Zoneset> zonesets) {
        log.info "Persist ${zonesets.size()} zonesets for NX-OS switch $nxOsSwitch"
        nxOsSwitch.zonesets*.delete()
        nxOsSwitch.zonesets.clear()
        zonesets.each { Zoneset zoneset ->
            nxOsSwitch.addToZonesets(zoneset)
            zoneset.nxOsSwitch = nxOsSwitch
            zoneset.save()
        }
        nxOsSwitch.save()
    }

    public Collection<Zoneset> getZones(NxOsSwitch nxOsSwitch, boolean persist = true) {
        String command = getCommand("Zones.txt")
        String commandResult = sshService.runCommand(nxOsSwitch.ip, nxOsSwitch.credential.username, nxOsSwitch.credential.password, command)
        Collection<Zoneset> zonesets = nxOsSwitchResponseInterpreterService.interpretZonesResponse(commandResult)
        if(persist) {
            persistZones(nxOsSwitch, zonesets)
            nxOsSwitch.zonesets
        } else {
            zonesets
        }
    }
}
