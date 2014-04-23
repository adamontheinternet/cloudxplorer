<%@ page import="com.cx.domain.Credential" %>

<div class="fieldcontain ${hasErrors(bean: credentialInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="credential.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${credentialInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: credentialInstance, field: 'password', 'error')} ">
	<label for="password">
		<g:message code="credential.password.label" default="Password" />
		
	</label>
	<g:textField name="password" value="${credentialInstance?.password}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: credentialInstance, field: 'username', 'error')} ">
	<label for="username">
		<g:message code="credential.username.label" default="Username" />
		
	</label>
	<g:textField name="username" value="${credentialInstance?.username}"/>

</div>

