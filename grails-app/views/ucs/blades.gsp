<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 3/27/14
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
  <meta name="layout" content="main">
</head>
<body>
<h1>Blades for UCS <g:link controller="ucs" action="show" id="${ucsInstance?.id}">${ucsInstance.ip}</g:link></h1>
    <g:if test="${error}">
        <ul class="errors" role="alert">
            <li>${error}</li>
        </ul>
    </g:if>
<table>
    <tr><th>DN</th><th>Assigned To</th></tr>
    <g:each in="${blades}" var="blade">
        <tr><td>${blade.dn}</td><td>${blade.assignedTo}</td></tr>
    </g:each>
</table>
</body>
</html>