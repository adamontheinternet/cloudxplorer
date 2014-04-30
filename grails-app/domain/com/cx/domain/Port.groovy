package com.cx.domain

class Port implements Searchable {

    String wwn

    static constraints = {
    }

    static belongsTo = [zone:Zone]

    public String getType() {
        "Port"
    }

    public String getFullyQualifiedName() {
        zone.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$wwn"
    }
}
