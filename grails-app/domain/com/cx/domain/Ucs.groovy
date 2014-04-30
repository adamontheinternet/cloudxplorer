package com.cx.domain

class Ucs extends CloudElement {

    static hasMany = [blades:Blade, servers:Server, vlans:Vlan, vsans:Vsan]

    static constraints = {
    }

    public String getType() {
        "UCS"
    }

    public String getFullyQualifiedName() {
        this.toString()
    }
}
