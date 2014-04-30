package com.cx.domain

class Server implements Searchable {

    String dn
    String assignState
    String configState
    String operState
    String assocState

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }

    public String getType() {
        "Server"
    }

    public String getFullyQualifiedName() {
        ucs.getFullyQualifiedName() + " -> " + "${this.toString()}"
    }

    public String toString() {
        "${getType()}:$dn"
    }
}
