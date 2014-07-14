<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 6/20/14
  Time: 3:02 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${vcenterInstance} Disks</title>
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="show" action="show" id="${vcenterInstance.id}">Back to ${vcenterInstance.ip}</g:link></li>
    </ul>
</div>

<g:if test="${error}">
    <ul class="errors" role="alert">
        <li>${error}</li>
    </ul>
</g:if>

<table>
    <tr><th>Name</th><th>Cluster</th><th>OS</th><th>Power State</th><th>Connection State</th><th>Maintenance Mode</th></tr>
    <g:each in="${hosts.sort{it.name}}" var="host">
        <tr><td>${host.name}</td><td>${host.cluster}</td><td>${host.os}</td><td>${host.powerState}</td><td>${host.connectionState}</td><td>${host.maintenanceMode}</td></tr>
    </g:each>
</table>

</body>
</html>