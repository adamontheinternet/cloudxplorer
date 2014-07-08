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
    <tr><th>Name (Host_LUN)</th><th>UUID</th><th>LUN</th><th>Disk Capacity</th><th>Datastore</th><th>Datastore Capacity</th><th>Datastore Version</th><th>Datastore Block Size</th></tr>
    <g:each in="${disks}" var="disk">
        <tr><td>${disk.name}</td><td>${disk.uuid}</td><td>${disk.lun}</td><td>${disk.capacity}</td><td>${disk.datastore}</td><td>${disk.datastoreCapacity}</td><td>${disk.datastoreVersion}</td><td>${disk.datastoreBlockSize}</td></tr>
    </g:each>
</table>

</body>
</html>