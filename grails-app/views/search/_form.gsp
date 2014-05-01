<%@ page import="com.cx.domain.Search" %>



<div class="fieldcontain ${hasErrors(bean: searchInstance, field: 'nxOsSwitch', 'error')} ">
	<label for="nxOsSwitch">
		<g:message code="search.nxOsSwitch.label" default="Nx Os Switch" />
		
	</label>
	<g:checkBox name="nxOsSwitch" value="${searchInstance?.nxOsSwitch}" checked="true"/>

</div>

<div class="fieldcontain ${hasErrors(bean: searchInstance, field: 'ucs', 'error')} ">
	<label for="ucs">
		<g:message code="search.ucs.label" default="Ucs" />
		
	</label>
	<g:checkBox name="ucs" value="${searchInstance?.ucs}" checked="true"/>

</div>

<div class="fieldcontain ${hasErrors(bean: searchInstance, field: 'vcenter', 'error')} ">
	<label for="vcenter">
		<g:message code="search.vcenter.label" default="Vcenter" />
		
	</label>
	<g:checkBox name="vcenter" value="${searchInstance?.vcenter}" checked="true"/>

</div>

<div class="fieldcontain ${hasErrors(bean: searchInstance, field: 'value', 'error')} ">
    <label for="value">
        <g:message code="search.value.label" default="Value" />

    </label>
    <g:textField name="value" value="${searchInstance?.value}"/>

</div>

