
<%@ page import="com.cx.domain.Vcenter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'vcenter.label', default: 'Vcenter')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-vcenter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
                <li><g:link action="index">Back to all vCenter</g:link></li>
                <li><g:link class="list" action="virtualMachines" resource="${vcenterInstance}"><g:message code="default.vms.label" default="Virtual Machines" /></g:link></li>
                <li><g:link class="list" action="hosts" resource="${vcenterInstance}"><g:message code="default.hosts.label" default="Hosts" /></g:link></li>
                <li><g:link class="list" action="disks" resource="${vcenterInstance}"><g:message code="default.disks.label" default="Disks" /></g:link></li>
            </ul>
		</div>
		<div id="show-vcenter" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list vcenter">
			
				<g:if test="${vcenterInstance?.ip}">
				<li class="fieldcontain">
					<span id="ip-label" class="property-label"><g:message code="vcenter.ip.label" default="Ip" /></span>
					
						<span class="property-value" aria-labelledby="ip-label"><g:fieldValue bean="${vcenterInstance}" field="ip"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${vcenterInstance?.credential}">
				<li class="fieldcontain">
					<span id="credential-label" class="property-label"><g:message code="vcenter.credential.label" default="Credential" /></span>
					
						<span class="property-value" aria-labelledby="credential-label"><g:link controller="credential" action="show" id="${vcenterInstance?.credential?.id}">${vcenterInstance?.credential?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span id="connectionVerified-label" class="property-label"><g:message code="vcenter.connectionVerified.label" default="Connection Verified" /></span>
                    <span class="property-value" aria-labelledby="connectionVerified-label"><g:if test="${vcenterInstance.connectionVerified}"><img src="${resource(dir: 'images', file: 'AlertNormal_16.png')}" alt="CloudXplorer"/></g:if><g:else><img src="${resource(dir: 'images', file: 'AlertCritical.gif')}" alt="CloudXplorer"/></g:else></span>
				</li>

			</ol>
			<g:form url="[resource:vcenterInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${vcenterInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
