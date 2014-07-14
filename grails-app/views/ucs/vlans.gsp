<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 3/27/14
  Time: 2:43 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${ucsInstance} VLANs</title>
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="show" action="show" id="${ucsInstance.id}">Back to ${ucsInstance.ip}</g:link></li>
    </ul>
</div>

<g:if test="${error}">
    <ul class="errors" role="alert">
        <li>${error}</li>
    </ul>
</g:if>

<table>
    <tr><th>ID</th><th>Name</th></tr>
    <g:each in="${vlans.sort{it.networkId}}" var="vlan">
        <tr><td>${vlan.networkId}</td><td>${vlan.name}</td></tr>
    </g:each>
</table>
</body>
</html>