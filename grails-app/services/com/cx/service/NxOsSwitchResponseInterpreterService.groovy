package com.cx.service

import com.cx.domain.Port
import com.cx.domain.Vsan
import com.cx.domain.Zone
import com.cx.domain.Zoneset
import grails.transaction.Transactional

@Transactional
class NxOsSwitchResponseInterpreterService {

    public String interpretVersionResponse(String commandResult) {
        String firstLine = commandResult.split("\n")[0]
        log.info "First line of version command is $firstLine"
        firstLine
    }

    // Unsaved instances
    public Collection<Vsan> interpretVsansResponse(String commandResult) {
        Collection<Vsan> vsans = []
        String[] resultStrings = commandResult.split()
        Vsan vsan
        for(int i = 0; i < resultStrings.length; i++) {
            if(resultStrings[i] == "vsan") {
                // vsan $vsan information
                //  i    i+1     i+2
                vsan = new Vsan()
                vsan.vsan = resultStrings[i + 1]
            }
            if(resultStrings[i].startsWith("name:")) {
                // name:$name state:$state
                vsan.name = resultStrings[i].split(":")[1]
//                vsan.save()
                vsans << vsan
            }

        }
        return vsans
    }

    // Unsaved instances
    public Collection<Zoneset> interpretZonesResponse(String commandResult) {
        Collection<Zoneset> zonesets = []
        String[] resultStrings = commandResult.split()
        Zoneset zoneset
        Zone zone
        for(int i = 0; i < resultStrings.length; i++) {
            if(resultStrings[i] == "zoneset") {
                // zoneset name $name vsan $vsan
                //   i     i+1   i+2  i+3   i+4
                zoneset = new Zoneset()
                zoneset.name = resultStrings[i + 2]
                zoneset.vsan = resultStrings[i + 4]
//                zoneset.save()
                zonesets << zoneset
            }
            if(resultStrings[i] == "zone") {
                // zone name $name vsan $vsan
                zone = new Zone()
                zone.name = resultStrings[i + 2]
                zone.vsan = resultStrings[i + 4]
                zoneset.addToZones(zone)
                zone.zoneset = zoneset
//                zone.save()
            }
            if(resultStrings[i] == "pwwn") {
                // pwwn $wwn
                //  i   i+1
                Port port = new Port()
                port.wwn = resultStrings[i + 1]
                zone.addToPorts(port)
                port.zone = zone
//                port.save()
            }

        }
        return zonesets
    }
}
