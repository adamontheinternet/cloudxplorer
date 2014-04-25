
<%@ page import="com.cx.domain.NxOsSwitch" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nxOsSwitch.label', default: 'NX-OS Switch')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-nxOsSwitch" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
                <li><g:link action="index">Back to all NX-OS Switch</g:link></li>
                <li><g:link class="list" action="zones" resource="${nxOsSwitchInstance}"><g:message code="default.zones.label" default="Zones" /></g:link></li>
                <li><g:link class="list" action="vsans" resource="${nxOsSwitchInstance}"><g:message code="default.vsans.label" default="VSANs" /></g:link></li>
			</ul>
		</div>
		<div id="show-nxOsSwitch" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nxOsSwitch">
			
				<g:if test="${nxOsSwitchInstance?.ip}">
				<li class="fieldcontain">
					<span id="ip-label" class="property-label"><g:message code="nxOsSwitch.ip.label" default="Ip" /></span>
					
						<span class="property-value" aria-labelledby="ip-label"><g:fieldValue bean="${nxOsSwitchInstance}" field="ip"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nxOsSwitchInstance?.credential}">
				<li class="fieldcontain">
					<span id="credential-label" class="property-label"><g:message code="nxOsSwitch.credential.label" default="Credential" /></span>
					
						<span class="property-value" aria-labelledby="credential-label"><g:link controller="credential" action="show" id="${nxOsSwitchInstance?.credential?.id}">${nxOsSwitchInstance?.credential?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span id="connectionVerified-label" class="property-label"><g:message code="nxOsSwitch.connectionVerified.label" default="Connection Verified" /></span>
                    <span class="property-value" aria-labelledby="connectionVerified-label"><g:if test="${nxOsSwitchInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></span>
				</li>
			
			</ol>
			<g:form url="[resource:nxOsSwitchInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${nxOsSwitchInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
