package com.cx.domain

class Zoneset implements Searchable {

    String name
    String vsan

    static constraints = {
    }

    static hasMany = [zones:Zone]
    static belongsTo = [nxOsSwitch:NxOsSwitch]

    public String getType() {
        "Zoneset"
    }

    public String getFullyQualifiedName() {
        nxOsSwitch.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$name"
    }
}
