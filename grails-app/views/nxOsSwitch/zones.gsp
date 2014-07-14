<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 4/3/14
  Time: 4:37 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${nxOsSwitchInstance} Zones</title>
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="show" action="show" id="${nxOsSwitchInstance.id}">Back to ${nxOsSwitchInstance.ip}</g:link></li>
    </ul>
</div>

<g:if test="${error}">
    <ul class="errors" role="alert">
        <li>${error}</li>
    </ul>
</g:if>

<table>
    <tr><th>VSAN</th><th>Name</th><th>Ports</th><th>Zoneset Name</th></tr>
    <g:each in="${zonesets.sort{it.vsan}}" var="zoneset">
        <g:each in="${zoneset.zones}" var="zone">
            <tr><td>${zone.vsan}</td><td>${zone.name}</td><td>${zone.ports.collect{it.wwn}}</td><td>${zoneset.name}</td></tr>
        </g:each>
    </g:each>
</table>
</body>
</html>