package com.cx.domain

class Blade implements Searchable {

    String dn
    String assignedTo

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }

    static mapping = {
        sort "dn"
    }

    public String getType() {
        "Blade"
    }

    public String getFullyQualifiedName() {
        ucs.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$dn"
    }
}
