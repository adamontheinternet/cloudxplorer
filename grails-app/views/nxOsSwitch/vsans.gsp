<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 4/8/14
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${nxOsSwitchInstance} VSANs</title>
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
    <tr><th>VSAN</th><th>Name</th></tr>
    <g:each in="${vsans.sort{it.vsan}}" var="vsan">
        <tr><td>${vsan.vsan}</td><td>${vsan.name}</td></tr>
    </g:each>
</table>
</body>
</html>