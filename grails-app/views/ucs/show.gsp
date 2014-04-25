
<%@ page import="com.cx.domain.Ucs" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'ucs.label', default: 'Ucs')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-ucs" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link action="index">Back to all UCS</g:link></li>
                <li><g:link class="list" action="blades" resource="${ucsInstance}"><g:message code="default.blades.label" default="Blades" /></g:link></li>
                <li><g:link class="list" action="servers" resource="${ucsInstance}"><g:message code="default.servers.label" default="Servers" /></g:link></li>
                <li><g:link class="list" action="vlans" resource="${ucsInstance}"><g:message code="default.vlans.label" default="Vlans" /></g:link></li>

            </ul>
        </div>
		<div id="show-ucs" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list ucs">
			
				<g:if test="${ucsInstance?.ip}">
				<li class="fieldcontain">
					<span id="ip-label" class="property-label"><g:message code="ucs.ip.label" default="Ip" /></span>
					
						<span class="property-value" aria-labelledby="ip-label"><g:fieldValue bean="${ucsInstance}" field="ip"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${ucsInstance?.credential}">
				<li class="fieldcontain">
					<span id="credential-label" class="property-label"><g:message code="ucs.credential.label" default="Credential" /></span>
					
						<span class="property-value" aria-labelledby="credential-label"><g:link controller="credential" action="show" id="${ucsInstance?.credential?.id}">${ucsInstance?.credential?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span id="connectionVerified-label" class="property-label"><g:message code="ucs.connectionVerified.label" default="Connection Verified" /></span>
					<span class="property-value" aria-labelledby="connectionVerified-label"><g:if test="${ucsInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></span>
					
				</li>

			</ol>
			<g:form url="[resource:ucsInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${ucsInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
