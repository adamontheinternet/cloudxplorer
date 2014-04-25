package com.cx.domain

/**
 * Created with IntelliJ IDEA.
 * User: alaplante
 * Date: 4/22/14
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
class NxOsSwitch extends CloudElement {
    static constraints = {
    }

    static hasMany = [zonesets:Zoneset, vsans:Vsan]

    String getType() {
        "NX-OS Switch"
    }
}
