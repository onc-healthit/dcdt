<%@page import="org.apache.commons.collections.EnumerationUtils"%>
<%@ page import="gov.hhs.onc.dcdt.web.startup.ConfigInfo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%
	if (!EnumerationUtils.toList(session.getAttributeNames()).contains("HOSTING_TESTCASES"))
	{
		session.setAttribute("HOSTING_TESTCASES", ConfigInfo.getHostingTestcases());
	}
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/scripts/testcases.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/scripts/hosting.js"></script>
<script type="text/javascript">
$(document).ready(function () {
	window.HOSTING_TESTCASES = {};
	
	<c:forEach var="hostingTestcaseId" varStatus="hostingTestcasesStatus" items="${sessionScope.HOSTING_TESTCASES.keySet()}">
		<c:set var="hostingTestcase" value="${sessionScope.HOSTING_TESTCASES[hostingTestcaseId]}"/>
		<c:set var="hostingTestcaseComments" value="${hostingTestcase.comments}"/>
		
		window.HOSTING_TESTCASES["${hostingTestcaseId}"] = {
			id: "${hostingTestcaseId}", 
			name: "${hostingTestcase.name}", 
			location: "${hostingTestcase.location.name}", 
			binding: "${hostingTestcase.binding.name}", 
			comments: {
				shortDescription: "${hostingTestcaseComments.shortDescription}", 
				description: "${hostingTestcaseComments.description}", 
				instructions: "${hostingTestcaseComments.instructions}", 
				rtm: "${hostingTestcaseComments.rtm}", 
				specifications: "${hostingTestcaseComments.specifications}"
			}
		};
	</c:forEach>
});
</script>