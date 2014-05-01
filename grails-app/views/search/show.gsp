
<%@ page import="com.cx.domain.Search" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'search.label', default: 'Search')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-search" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-search" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list search">
			
				<g:if test="${searchInstance?.nxOsSwitch}">
				<li class="fieldcontain">
					<span id="nxOsSwitch-label" class="property-label"><g:message code="search.nxOsSwitch.label" default="Nx Os Switch" /></span>
					
						<span class="property-value" aria-labelledby="nxOsSwitch-label"><g:formatBoolean boolean="${searchInstance?.nxOsSwitch}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${searchInstance?.ucs}">
				<li class="fieldcontain">
					<span id="ucs-label" class="property-label"><g:message code="search.ucs.label" default="Ucs" /></span>
					
						<span class="property-value" aria-labelledby="ucs-label"><g:formatBoolean boolean="${searchInstance?.ucs}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${searchInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="search.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${searchInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${searchInstance?.vcenter}">
				<li class="fieldcontain">
					<span id="vcenter-label" class="property-label"><g:message code="search.vcenter.label" default="Vcenter" /></span>
					
						<span class="property-value" aria-labelledby="vcenter-label"><g:formatBoolean boolean="${searchInstance?.vcenter}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:searchInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${searchInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
