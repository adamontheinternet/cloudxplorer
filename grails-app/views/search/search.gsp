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

<table>
    <tr><th>Type</th><th>Fully Qualified Name</th></tr>
    <g:each in="${objects}" var="object">
        <tr><td>${object.getType()}</td><td>${object.getFullyQualifiedName()}</td></tr>
    </g:each>
</table>

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