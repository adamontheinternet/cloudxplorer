package com.cx.domain

class Vsan {

    String vsan
    String name
    String dn
    String switchId

    static constraints = {
        // Will be on or the other...
        ucs nullable:true
        nxOsSwitch nullable:true

        // UCS only
        dn nullable:true
        switchId nullable:true
    }

    static belongsTo = [nxOsSwitch:NxOsSwitch, ucs:Ucs]
}
