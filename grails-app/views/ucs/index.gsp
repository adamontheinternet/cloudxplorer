
<%@ page import="com.cx.domain.Ucs" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'ucs.label', default: 'Ucs')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-ucs" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
                <li><a href="${createLink(uri: '/')}">Back to /</a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-ucs" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="ip" title="${message(code: 'ucs.ip.label', default: 'Ip')}" />
					
						<th><g:message code="ucs.credential.label" default="Credential" /></th>
					
						<g:sortableColumn property="connectionVerified" title="${message(code: 'ucs.connectionVerified.label', default: 'Connection Verified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${ucsInstanceList}" status="i" var="ucsInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${ucsInstance.id}">${fieldValue(bean: ucsInstance, field: "ip")}</g:link></td>
					
						<td>${fieldValue(bean: ucsInstance, field: "credential")}</td>

                        <td><g:if test="${ucsInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></td>

					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${ucsInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
