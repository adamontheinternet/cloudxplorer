package com.cx.domain

class Zone implements Searchable {

    String name
    String vsan

    static constraints = {
    }

    static belongsTo = [zoneset:Zoneset]
    static hasMany = [ports:Port]

    public String getType() {
        "Zone"
    }

    public String getFullyQualifiedName() {
        zoneset.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$name"
    }
}
