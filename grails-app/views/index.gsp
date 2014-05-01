<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Cloud Xplorer</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}

			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 2em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="page-body" role="main">
            <h1>Welcome to Cloud Xplorer</h1>
            <p><a href="/CloudXplorer/credential">Create credentials</a> and then start exploring the devices that power the cloud! Support currently exists for <a href="/CloudXplorer/ucs">Cisco UCS B series</a>,
                <a href="/CloudXplorer/nxOsSwitch">Cisco NX-OS based switches</a>, and <a href="/CloudXplorer/vcenter">VMware vCenter Servers</a>. Or if you are looking for something in particular then
                <a href="/CloudXplorer/search">try a search</a>.</p>

            <a href="/CloudXplorer/ucs"><img src="${resource(dir: 'images', file: 'cisco_ucs_manager_small.png')}" alt="UcsLogo"/></a>
            <a href="/CloudXplorer/nxOsSwitch"><img src="${resource(dir: 'images', file: 'cisco_switch_small.png')}" alt="SwitchLogo"/></a>
            <a href="/CloudXplorer/vcenter"><img src="${resource(dir: 'images', file: 'vcenter_server_logo2.png')}" alt="VcenterLogo"/></a>
            <a href="/CloudXplorer/search"><img src="${resource(dir: 'images', file: 'search.png')}" alt="SearchLogo"/></a>
            <a href="/CloudXplorer/credential"><img src="${resource(dir: 'images', file: 'key-icon.png')}" alt="CredentialLogo"/></a>
        </div>
	</body>
</html>
