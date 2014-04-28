<%@ page import="com.cx.domain.Vcenter" %>



<div class="fieldcontain ${hasErrors(bean: vcenterInstance, field: 'ip', 'error')} ">
	<label for="ip">
		<g:message code="vcenter.ip.label" default="Ip" />
		
	</label>
	<g:textField name="ip" value="${vcenterInstance?.ip}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: vcenterInstance, field: 'credential', 'error')} ">
	<label for="credential">
		<g:message code="vcenter.credential.label" default="Credential" />
		
	</label>
	<g:select id="credential" name="credential.id" from="${com.cx.domain.Credential.list()}" optionKey="id" value="${vcenterInstance?.credential?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>


