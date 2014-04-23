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
  <title></title>
    <meta name="layout" content="main">
</head>
<body>
<h1>VLANs for UCS <g:link controller="ucs" action="show" id="${ucsInstance?.id}">${ucsInstance.ip}</g:link></h1>
<g:if test="${error}">
    <ul class="errors" role="alert">
        <li>${error}</li>
    </ul>
</g:if>
<table>
    <tr><th>ID</th><th>Name</th></tr>
    <g:each in="${vlans}" var="vlan">
        <tr><td>${vlan.id}</td><td>${vlan.name}</td></tr>
    </g:each>
</table>
</body>
</html>