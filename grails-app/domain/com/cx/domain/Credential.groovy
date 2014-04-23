package com.cx.domain

class Credential {

    String name
    String username
    String password

    static hasMany = [cloudElements:CloudElement]

    static constraints = {
    }

    String toString() {
        name
    }
}
