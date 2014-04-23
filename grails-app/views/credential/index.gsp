
<%@ page import="com.cx.domain.Credential" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'credential.label', default: 'Credential')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-credential" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-credential" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'credential.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="password" title="${message(code: 'credential.password.label', default: 'Password')}" />
					
						<g:sortableColumn property="username" title="${message(code: 'credential.username.label', default: 'Username')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${credentialInstanceList}" status="i" var="credentialInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${credentialInstance.id}">${fieldValue(bean: credentialInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: credentialInstance, field: "password")}</td>
					
						<td>${fieldValue(bean: credentialInstance, field: "username")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${credentialInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
