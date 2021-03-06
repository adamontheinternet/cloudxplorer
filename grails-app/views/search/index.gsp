
<%@ page import="com.cx.domain.Search" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'search.label', default: 'Search')}" />
		<title>Search</title>
	</head>
	<body>
		<a href="#list-search" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
                <li><a href="${createLink(uri: '/')}">Back to /</a></li>
                <li><g:link action="load">Refresh Device Data</g:link></li>
			</ul>
		</div>
		<div id="list-search" class="content scaffold-list" role="main">
            <div id="create-search" class="content scaffold-create" role="main">
                <h1>Refresh Device Data (can take several minutes) then perform a search</h1>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="errors" role="alert">${flash.error}</div>
                </g:if>

                <g:form url="[resource:searchInstance, action:'search']" >
                    <fieldset class="form">
                        <g:render template="form"/>
                    </fieldset>
                    <fieldset class="buttons">
                        <g:submitButton name="search" class="save" value="${message(code: 'default.button.search.label', default: 'Search')}" />
                    </fieldset>
                </g:form>
            </div>
		</div>
	</body>
</html>
