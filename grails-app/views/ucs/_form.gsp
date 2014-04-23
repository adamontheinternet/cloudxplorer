<%@ page import="com.cx.domain.Ucs" %>



<div class="fieldcontain ${hasErrors(bean: ucsInstance, field: 'ip', 'error')} ">
	<label for="ip">
		<g:message code="ucs.ip.label" default="Ip" />
		
	</label>
	<g:textField name="ip" value="${ucsInstance?.ip}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: ucsInstance, field: 'credential', 'error')} ">
	<label for="credential">
		<g:message code="ucs.credential.label" default="Credential" />
		
	</label>
	<g:select id="credential" name="credential.id" from="${com.cx.domain.Credential.list()}" optionKey="id" value="${ucsInstance?.credential?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

