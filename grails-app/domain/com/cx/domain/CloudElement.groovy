package com.cx.domain

abstract class CloudElement implements Searchable {

    String ip
    Boolean connectionVerified = false
    Credential credential // no belongsTo because we dont want the CloudElement cascade deleted

    static constraints = {
        ip unique:true
        credential nullable:true
    }

    abstract String getType()

    public String toString() {
        "${getType()}:$ip"
    }
}
