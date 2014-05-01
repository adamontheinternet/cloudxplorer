
<%@ page import="com.cx.domain.Vcenter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'vcenter.label', default: 'Vcenter')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-vcenter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
                <li><a href="${createLink(uri: '/')}">Back to /</a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-vcenter" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="ip" title="${message(code: 'vcenter.ip.label', default: 'Ip')}" />
					
						<th><g:message code="vcenter.credential.label" default="Credential" /></th>
					
						<g:sortableColumn property="connectionVerified" title="${message(code: 'vcenter.connectionVerified.label', default: 'Connection Verified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${vcenterInstanceList}" status="i" var="vcenterInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${vcenterInstance.id}">${fieldValue(bean: vcenterInstance, field: "ip")}</g:link></td>
					
						<td>${fieldValue(bean: vcenterInstance, field: "credential")}</td>
					
                        <td><g:if test="${vcenterInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${vcenterInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
