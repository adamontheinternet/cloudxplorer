package com.cx.domain

class Vsan {

    String vsan
    String name

    static constraints = {
    }

    static belongsTo = [nxOsSwitch:NxOsSwitch]
}
