
<%@ page import="com.cx.domain.Credential" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'credential.label', default: 'Credential')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-credential" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-credential" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list credential">

				<g:if test="${credentialInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="credential.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${credentialInstance}" field="name"/></span>
					
				</li>
				</g:if>

                <g:if test="${credentialInstance?.username}">
                    <li class="fieldcontain">
                        <span id="username-label" class="property-label"><g:message code="credential.username.label" default="Username" /></span>

                        <span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${credentialInstance}" field="username"/></span>

                    </li>
                </g:if>

                <g:if test="${credentialInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="credential.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${credentialInstance}" field="password"/></span>
					
				</li>
				</g:if>


                <g:if test="${credentialInstance?.cloudElements}">
                    <li class="fieldcontain">
                        <span id="cloudElements-label" class="property-label"><g:message code="credential.cloudElements.label" default="Used By" /></span>

                            <span class="property-value" aria-labelledby="cloudElements-label"><g:fieldValue bean="${credentialInstance}" field="cloudElements"/></span>

                    </li>
                </g:if>


            </ol>
			<g:form url="[resource:credentialInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${credentialInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
