
<%@ page import="com.cx.domain.NxOsSwitch" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nxOsSwitch.label', default: 'NX-OS Switch')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-nxOsSwitch" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-nxOsSwitch" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="ip" title="${message(code: 'nxOsSwitch.ip.label', default: 'Ip')}" />
					
						<th><g:message code="nxOsSwitch.credential.label" default="Credential" /></th>
					
						<g:sortableColumn property="connectionVerified" title="${message(code: 'nxOsSwitch.connectionVerified.label', default: 'Connection Verified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nxOsSwitchInstanceList}" status="i" var="nxOsSwitchInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nxOsSwitchInstance.id}">${fieldValue(bean: nxOsSwitchInstance, field: "ip")}</g:link></td>
					
						<td>${fieldValue(bean: nxOsSwitchInstance, field: "credential")}</td>
					
                        <td><g:if test="${nxOsSwitchInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></td>

                    </tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nxOsSwitchInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
