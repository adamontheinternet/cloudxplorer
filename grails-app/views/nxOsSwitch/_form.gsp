<%@ page import="com.cx.domain.NxOsSwitch" %>



<div class="fieldcontain ${hasErrors(bean: nxOsSwitchInstance, field: 'ip', 'error')} ">
	<label for="ip">
		<g:message code="nxOsSwitch.ip.label" default="Ip" />
		
	</label>
	<g:textField name="ip" value="${nxOsSwitchInstance?.ip}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: nxOsSwitchInstance, field: 'credential', 'error')} ">
	<label for="credential">
		<g:message code="nxOsSwitch.credential.label" default="Credential" />
		
	</label>
	<g:select id="credential" name="credential.id" from="${com.cx.domain.Credential.list()}" optionKey="id" value="${nxOsSwitchInstance?.credential?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

