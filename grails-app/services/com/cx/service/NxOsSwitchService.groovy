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

    public Collection<Vsan> getVsans(NxOsSwitch nxOsSwitch) {
        String command = getCommand("Vsans.txt")
        String commandResult = sshService.runCommand(nxOsSwitch.ip, nxOsSwitch.credential.username, nxOsSwitch.credential.password, command)
        Collection<Vsan> vsans = nxOsSwitchResponseInterpreterService.interpretVsansResponse(commandResult)
        nxOsSwitch.vsans*.delete()
        nxOsSwitch.vsans.clear()
        vsans.each { Vsan vsan ->
            nxOsSwitch.addToVsans(vsan)
            vsan.nxOsSwitch = nxOsSwitch
            vsan.save()
        }
        nxOsSwitch.save()
        nxOsSwitch.vsans
    }

    public Collection<Zoneset> getZones(NxOsSwitch nxOsSwitch) {
        String command = getCommand("Zones.txt")
        String commandResult = sshService.runCommand(nxOsSwitch.ip, nxOsSwitch.credential.username, nxOsSwitch.credential.password, command)
        Collection<Zoneset> zonesets = nxOsSwitchResponseInterpreterService.interpretZonesResponse(commandResult)
        nxOsSwitch.zonesets*.delete()
        nxOsSwitch.zonesets.clear()
        zonesets.each { Zoneset zoneset ->
            nxOsSwitch.addToZonesets(zoneset)
            zoneset.nxOsSwitch = nxOsSwitch
            zoneset.save()
        }
        nxOsSwitch.save()
        nxOsSwitch.zonesets
    }
}
