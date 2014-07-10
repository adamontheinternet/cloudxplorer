<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 5/1/14
  Time: 1:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${searchInstance.value} Results</title>
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link action="index">Back to another search</g:link></li>
    </ul>
</div>

<g:if test="${error}">
    <ul class="errors" role="alert">
        <li>${error}</li>
    </ul>
</g:if>


<g:each in="${searchResult.getBlades()}" var="ucsBlades">
    <h1>${ucsBlades.key} Blades</h1>
    <table>
        <tr><th>DN</th><th>Assigned To</th></tr>
        <g:each in="${ucsBlades.value}" var="blade">
            <tr><td>${blade.dn}</td><td>${blade.assignedTo}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getServers()}" var="ucsServers">
    <h1>${ucsServers.key} Servers</h1>
    <table>
        <tr><th>DN</th><th>Assign State</th><th>Config State</th><th>Oper State</th><th>Assoc State</th></tr>
        <g:each in="${ucsServers.value}" var="server">
            <tr><td>${server.dn}</td><td>${server.assignState}</td><td>${server.configState}</td><td>${server.operState}</td><td>${server.assocState}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getVlans()}" var="ucsVlans">
    <h1>${ucsVlans.key} Vlans</h1>
    <table>
        <tr><th>ID</th><th>Name</th></tr>
        <g:each in="${ucsVlans.value}" var="vlan">
            <tr><td>${vlan.networkId}</td><td>${vlan.name}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getUcsVsans()}" var="ucsVsans">
    <h1>${ucsVsans.key} Vsans</h1>
    <table>
        <tr><th>ID</th><th>Name</th><th>DN</th><th>Switch</th></tr>
        <g:each in="${ucsVsans.value}" var="vsan">
            <tr><td>${vsan.vsan}</td><td>${vsan.name}</td><td>${vsan.dn}</td><td>${vsan.switchId}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getZones()}" var="switchZones">
    <h1>${switchZones.key} Zones</h1>
    <table>
        <tr><th>VSAN</th><th>Name</th><th>Ports</th><th>Zoneset Name</th></tr>
        <g:each in="${switchZones.value}" var="zone">
            <tr><td>${zone.vsan}</td><td>${zone.name}</td><td>${zone.ports.collect{it.wwn}}</td><td>${zone.zoneset.name}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getNxOsSwitchVsans()}" var="switchVsans">
    <h1>${switchVsans.key} Vsans</h1>
    <table>
        <tr><th>VSAN</th><th>Name</th></tr>
        <g:each in="${switchVsans.value}" var="vsan">
            <tr><td>${vsan.vsan}</td><td>${vsan.name}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getVirtualMachines()}" var="vcenterVirtualMachines">
    <h1>${vcenterVirtualMachines.key} Virtual Machines</h1>
    <table>
        <tr><th>Name</th><th>Host</th><th>Power State</th><th>Template</th></tr>
        <g:each in="${vcenterVirtualMachines.value}" var="virtualMachine">
            <tr><td>${virtualMachine.name}</td><td>${virtualMachine.host}</td><td>${virtualMachine.powerState}</td><td>${virtualMachine.template}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getHosts()}" var="vcenterHosts">
    <h1>${vcenterHosts.key} Hosts</h1>
    <table>
        <tr><th>Name</th><th>Cluster</th><th>OS</th><th>Power State</th><th>Connection State</th><th>Maintenance Mode</th></tr>
        <g:each in="${vcenterHosts.value}" var="host">
            <tr><td>${host.name}</td><td>${host.cluster}</td><td>${host.os}</td><td>${host.powerState}</td><td>${host.connectionState}</td><td>${host.maintenanceMode}</td></tr>
        </g:each>
    </table>
</g:each>

<g:each in="${searchResult.getDisks()}" var="vcenterDisks">
    <h1>${vcenterDisks.key} Disks</h1>
    <table>
        <tr><th>Name (Host_LUN)</th><th>LUN</th><th>Datastore</th><th>Disk (Datastore) Capacity GB</th><th>Datastore Version (Block Size MB)</th><th>UUID</th></tr>
        <g:each in="${vcenterDisks.value}" var="disk">
            <g:if test="${disk.datastore}">
                <tr><td>${disk.name}</td><td>${disk.lun}</td><td>${disk.datastore}</td><td>${disk.capacity} (${disk.datastoreCapacity})</td><td>VMFS${disk.datastoreVersion} (${disk.datastoreBlockSize})</td><td>${disk.uuid}</td></tr>
            </g:if>
            <g:else>
                <tr><td>${disk.name}</td><td>${disk.lun}</td><td></td><td>${disk.capacity}</td><td></td><td>${disk.uuid}</td></tr>
            </g:else>        </g:each>
    </table>
</g:each>

<%--
<table>
    <tr><th>Type</th><th>Fully Qualified Name</th></tr>
    <g:each in="${objects}" var="object">
        <tr><td>${object.getType()}</td><td>${object.getFullyQualifiedName()}</td></tr>
    </g:each>
</table>
--%>

<%--
<g:if test="${blades?.size() > 0}">
    <h1>Blades</h1>
    <table>
        <tr><th>DN</th><th>Assigned To</th><th>Fully Qualified Name</th></tr>
        <g:each in="${blades}" var="blade">
            <tr><td>${blade.dn}</td><td>${blade.assignedTo}</td><td>${blade.getFullyQualifiedName()}</td></tr>
        </g:each>
    </table>
</g:if>

<g:if test="${servers?.size() > 0}">
    <h1>Servers</h1>
    <table>
        <tr><th>DN</th><th>Assign State</th><th>Config State</th><th>Oper State</th><th>Assoc State</th></tr>
        <g:each in="${servers}" var="server">
            <tr><td>${server.dn}</td><td>${server.assignState}</td><td>${server.configState}</td><td>${server.operState}</td><td>${server.assocState}</td></tr>
        </g:each>
    </table>
</g:if>

<g:if test="${vlans?.size() > 0}">
    <h1>VLANs</h1>
    <table>
        <tr><th>ID</th><th>Name</th></tr>
        <g:each in="${vlans}" var="vlan">
            <tr><td>${vlan.networkId}</td><td>${vlan.name}</td></tr>
        </g:each>
    </table>
</g:if>

<g:if test="${ucsVsans?.size() > 0}">
    <h1>UCS VSANs</h1>
    <table>
        <tr><th>ID</th><th>Name</th><th>DN</th><th>Switch</th></tr>
        <g:each in="${ucsVsans}" var="vsan">
            <tr><td>${vsan.vsan}</td><td>${vsan.name}</td><td>${vsan.dn}</td><td>${vsan.switchId}</td></tr>
        </g:each>
    </table>
</g:if>

<g:if test="${nxOsSwitchVsans?.size() > 0}">
    <h1>NX-OS Switch VSANs</h1>
    <table>
        <tr><th>VSAN</th><th>Name</th></tr>
        <g:each in="${nxOsSwitchVsans}" var="vsan">
            <tr><td>${vsan.vsan}</td><td>${vsan.name}</td></tr>
        </g:each>
    </table>
</g:if>

<g:if test="${zonesets?.size() > 0}">
    <h1>Zones</h1>
    <table>
        <tr><th>VSAN</th><th>Name</th><th>Ports</th><th>Zoneset Name</th></tr>
        <g:each in="${zonesets}" var="zoneset">
            <g:each in="${zoneset.zones}" var="zone">
                <tr><td>${zone.vsan}</td><td>${zone.name}</td><td>${zone.ports.collect{it.wwn}}</td><td>${zoneset.name}</td></tr>
            </g:each>
        </g:each>
    </table>
</g:if>
--%>

</body>
</html>