<%@ page import="gov.hhs.onc.dcdt.web.startup.ConfigInfo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<hr/>
<div class="container">
	<strong>
		If you have any questions about the tool, please check out our<a href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions" class="oncfaq"> FAQ page</a> or
		post a question to the tool's community <a href="mailto:directtesttool@googlegroups.com" class="mail">discussion group</a>.
	</strong>
	<br/><br/>

	<div id="version">
		<%
			pageContext.setAttribute("webModuleVersion", ConfigInfo.getConfig().getModuleVersion());
		%>
		<html:link action="/version"><span id="versionLink">Version</span></html:link>: ${webModuleVersion.version}
		(<span class="versionPartLabel">SVN</span>: url=${webModuleVersion.svnHeadUrl}, rev=${webModuleVersion.svnRevision} date=${webModuleVersion.svnDate})
		(<span class="versionPartLabel">Build</span>: date=${webModuleVersion.buildTimestamp})
	</div>
</div>
<br/>