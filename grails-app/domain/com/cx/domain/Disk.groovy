package com.cx.domain

class Disk {

    static constraints = {
        datastore nullable: true
        key nullable: true
        datastoreCapacity nullable:true
        datastoreVersion nullable:true
        datastoreBlockSize nullable:true
        uuid nullable: true
    }

    static belongsTo = [vcenter:Vcenter]

    String name
    String uuid
    String devicePath
    Integer capacity
    Integer lun
    String datastore
    Integer datastoreCapacity
    String datastoreVersion
    String datastoreBlockSize
    String key
    String host
    String cluster
}
