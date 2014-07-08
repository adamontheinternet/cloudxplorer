package com.cx.domain

class Vcenter extends CloudElement {

    static hasMany = [virtualMachines:VirtualMachine, hosts:Host, disks:Disk]

    static constraints = {
    }

    public String getType() {
        "vCenter"
    }

    public String getFullyQualifiedName() {
        this.toString()
    }
}
