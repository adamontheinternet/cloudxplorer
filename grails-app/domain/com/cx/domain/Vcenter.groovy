package com.cx.domain

class Vcenter extends CloudElement {

    static constraints = {
    }

    public String getType() {
        "vCenter"
    }

    public String getFullyQualifiedName() {
        this.toString()
    }
}
