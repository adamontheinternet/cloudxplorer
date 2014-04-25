package com.cx.domain

class Port {

    String wwn

    static constraints = {
    }

    static belongsTo = [zone:Zone]
}
