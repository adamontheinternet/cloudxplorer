package com.cx.domain

class Vlan implements Searchable {

    String dn
    String networkId
    String name

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }

    public String getType() {
        "VLAN"
    }

    public String getFullyQualifiedName() {
        ucs.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$name"
    }
}
