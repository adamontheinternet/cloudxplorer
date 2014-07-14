<%--
  Created by IntelliJ IDEA.
  User: alaplante
  Date: 6/20/14
  Time: 1:40 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${vcenterInstance} Virtual Machines</title>
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
    <tr><th>Name</th><th>Host</th><th>Power State</th><th>Template</th></tr>
    <g:each in="${virtualMachines.sort{it.name}}" var="virtualMachine">
        <tr><td>${virtualMachine.name}</td><td>${virtualMachine.host}</td><td>${virtualMachine.powerState}</td><td>${virtualMachine.template}</td></tr>
    </g:each>
</table>

</body>
</html>