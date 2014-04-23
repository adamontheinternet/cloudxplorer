package com.cx.domain

class Server {

    String dn
    String assignState
    String configState
    String operState
    String assocState

    static belongsTo = [ucs:Ucs]

    static constraints = {
    }
}
