package com.cx.domain

class Blade {

    String dn
    String assignedTo

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }

    static mapping = {
        sort "dn"
    }

    /*
    TODO Refactor to proper searchable interface common and clean across all domain objects
     */
    public String toString() {
        "Blade:$dn"
    }

    public String getFullyQualifiedPath() {
        ucs.toString() + "/" + this
    }
}
