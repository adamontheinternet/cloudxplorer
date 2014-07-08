package com.cx.domain

class VirtualMachine {

    String name
    Boolean template
    String powerState
    String cluster
    String key
    String host

    static constraints = {
        host nullable:true
    }
}
