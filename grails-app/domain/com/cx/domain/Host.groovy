package com.cx.domain

class Host {

    String name
    String cluster
    String key
    String os
    String powerState
    String connectionState
    Boolean maintenanceMode

    static belongsTo = [vcenter:Vcenter]

    static constraints = {
    }
}
