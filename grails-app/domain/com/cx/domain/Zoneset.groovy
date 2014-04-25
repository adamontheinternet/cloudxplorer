package com.cx.domain

class Zoneset {

    String name
    String vsan

    static constraints = {
    }

    static hasMany = [zones:Zone]
    static belongsTo = [nxOsSwitch:NxOsSwitch]
}
