package com.cx.domain

class Vsan implements Searchable {

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

    public String getType() {
        "VSAN"
    }

    public String getFullyQualifiedName() {
        // Will either be owned by UCS or switch
        (ucs?.getFullyQualifiedName() ?: nxOsSwitch?.getFullyQualifiedName()) + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$name"
    }
}
