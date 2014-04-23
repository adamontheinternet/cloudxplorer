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
}
