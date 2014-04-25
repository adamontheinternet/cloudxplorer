package com.cx.domain

class Ucs extends CloudElement {

    static hasMany = [blades:Blade, servers:Server, vlans:Vlan]

    static constraints = {
    }

    public String getType() {
        "UCS"
    }
}
