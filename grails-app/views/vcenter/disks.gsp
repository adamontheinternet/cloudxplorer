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
    <tr><th>Name (Host_LUN)</th><th>LUN</th><th>Datastore</th><th>Disk (Datastore) Capacity GB</th><th>Datastore Version (Block Size MB)</th><th>UUID</th></tr>
    <g:each in="${disks}" var="disk">
        <g:if test="${disk.datastore}">
            <tr><td>${disk.name}</td><td>${disk.lun}</td><td>${disk.datastore}</td><td>${disk.capacity} (${disk.datastoreCapacity})</td><td>VMFS${disk.datastoreVersion} (${disk.datastoreBlockSize})</td><td>${disk.uuid}</td></tr>
        </g:if>
        <g:else>
            <tr><td>${disk.name}</td><td>${disk.lun}</td><td></td><td>${disk.capacity}</td><td></td><td>${disk.uuid}</td></tr>
        </g:else>
    </g:each>
</table>

</body>
</html>