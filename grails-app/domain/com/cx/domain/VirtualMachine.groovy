package com.cx.domain

class VirtualMachine {

    String name
    Boolean template
    String powerState
    String cluster
    String key
    String host

    static belongsTo = [vcenter:Vcenter]

    static constraints = {
        host nullable:true
    }
}
