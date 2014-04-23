package com.cx.domain

class Ucs extends CloudElement {

    static hasMany = [blades:Blade, servers:Server, vlans:Vlan]

    static constraints = {
    }

    String getType() {
        "UCS"
    }
}
