<%@page import="gov.hhs.onc.dcdt.web.testcases.discovery.DiscoveryTestcasesContainer"%>
<%@page import="org.apache.commons.collections.EnumerationUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%
	if (!EnumerationUtils.toList(session.getAttributeNames()).contains("DISCOVERY_TESTCASES"))
	{
		session.setAttribute("DISCOVERY_TESTCASES", DiscoveryTestcasesContainer.getTestcases());
	}
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/scripts/discovery.js"></script>
<script type="text/javascript">
$(document).ready(function () {
	window.DISCOVERY_TESTCASES = {};
	
	<c:forEach var="discoveryTestcaseMail" varStatus="discoveryTestcasesStatus" items="${sessionScope.DISCOVERY_TESTCASES.keySet()}">
		<c:set var="discoveryTestcase" value="${sessionScope.DISCOVERY_TESTCASES[discoveryTestcaseMail]}"/>
		<c:set var="discoveryTestcaseComments" value="${discoveryTestcase.comments}"/>
		
		window.DISCOVERY_TESTCASES["${discoveryTestcase.id}"] = {
			id: "${discoveryTestcase.id}", 
			name: "${discoveryTestcase.name}", 
			mail: "${discoveryTestcaseMail}", 
			comments: {
				shortDescription: "${discoveryTestcaseComments.shortDescription}", 
				description: "${discoveryTestcaseComments.description}", 
				<c:if test="${not empty discoveryTestcaseComments.additionalInfo}">
					additionalInfo: "${discoveryTestcaseComments.additionalInfo}", 
				</c:if>
				targetCert: "${discoveryTestcaseComments.targetCert}", 
				backgroundCerts: "${discoveryTestcaseComments.backgroundCerts}", 
				rtm: "${discoveryTestcaseComments.rtm}", 
				specifications: "${discoveryTestcaseComments.specifications}"
			}
		};
	</c:forEach>
});

function setTextEmail(select_ele)
{
	document.getElementById("email").innerHTML = select_ele.options[select_ele.selectedIndex].value;
	var discoveryTestcaseId = select_ele.options[select_ele.selectedIndex].value;
	
	if (window.DISCOVERY_TESTCASES.hasOwnProperty(discoveryTestcaseId))
	{
		document.getElementById("email").innerHTML = window.DISCOVERY_TESTCASES[discoveryTestcaseId].mail;
	}
}
</script>