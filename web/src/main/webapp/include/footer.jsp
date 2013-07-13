<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<hr/>
<div class="container">
	<strong>
		If you have any questions about the tool, please check out our<a href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions" target="_blank"> FAQ page</a> or
		post a question to the tool's community <a href="mailto:directtesttool@googlegroups.com" class="mail">discussion group</a>.
	</strong>
	<br/><br/>

	<div id="version">
		<c:set var="webModuleVersion" value="${dcdt:moduleVersion('web')}"/>
		<html:link action="/version"><span id="versionLink">Version</span></html:link>:
		${webModuleVersion.version}
		(<span class="versionPartLabel">Mercurial</span>:
		branch/tag=<c:choose><c:when test="${not empty webModuleVersion.hgTag}">${webModuleVersion.hgTag}</c:when><c:otherwise>${webModuleVersion.hgBranch}</c:otherwise></c:choose>, 
        rev=${webModuleVersion.hgRevision} date=${webModuleVersion.hgDate})
		(<span class="versionPartLabel">Build</span>:
		date=${webModuleVersion.buildTimestamp})
	</div>
</div>
<br/>