<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<tiles:importAttribute name="nav_active" scope="request"/>
<logic:notEmpty name="nav_active">
	<script type="text/javascript">
	$(document).ready(function ()
	{
		$("#<%= request.getAttribute("nav_active") %>").addClass("active");
	});
	</script>
</logic:notEmpty>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<html:link action="/"><img src="${pageContext.request.contextPath}/static/images/dcdt_logo.png" class="brand" alt="DCDT Logo"/></html:link>
			<ul class="nav">
				<li id="home"><html:link action="/"><i class="icon-home icon-white"></i> Home</html:link></li>
				<li id="hosting"><html:link action="/hosting">Hosting</html:link></li>
				<li id="discovery"><html:link action="/discovery">Discovery</html:link></li>
			</ul>
			<ul class="nav pull-right">
				<li id="fat-menu" class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown">About <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li>
							<a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/User_Guide">
								User's Guide
							</a>
						</li>
						<li>
							<a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions">
								FAQ
							</a>
						</li>
						<li class="divider"></li>
						<li>
							<a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/">
								Code Repository
							</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
<br/><br/><br/><br/>