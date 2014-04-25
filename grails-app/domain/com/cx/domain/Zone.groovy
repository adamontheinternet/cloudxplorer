package com.cx.domain

class Zone {

    String name
    String vsan

    static constraints = {
    }

    static belongsTo = [zoneset:Zoneset]
    static hasMany = [ports:Port]
}
