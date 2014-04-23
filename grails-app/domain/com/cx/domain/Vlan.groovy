package com.cx.domain

class Vlan {

    String dn
    String networkId
    String name

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }
}
