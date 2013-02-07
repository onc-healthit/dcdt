<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'testcases.js')}"></script>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'discovery.js')}"></script>
<script type="text/javascript">
$(document).ready(function ()
{
	window.DISCOVERY_TESTCASES = {};
	
	<c:forEach var="discoveryTestcaseEntry" varStatus="discoveryTestcasesStatus" items="${dcdt:discoveryTestcases()}">
		window.DISCOVERY_TESTCASES["${discoveryTestcaseEntry.value.id}"] = {
			id: "${discoveryTestcaseEntry.value.id}", 
			name: "${discoveryTestcaseEntry.value.name}", 
			mail: "${discoveryTestcaseEntry.key}", 
			comments: {
				shortDescription: "${discoveryTestcaseEntry.value.comments.shortDescription}", 
				description: "${discoveryTestcaseEntry.value.comments.description}", 
				<c:if test="${not empty discoveryTestcaseEntry.value.comments.additionalInfo}">
					additionalInfo: "${discoveryTestcaseEntry.value.comments.additionalInfo}", 
				</c:if>
				targetCert: "${discoveryTestcaseEntry.value.comments.targetCert}", 
				backgroundCerts: "${discoveryTestcaseEntry.value.comments.backgroundCerts}", 
				rtm: "${discoveryTestcaseEntry.value.comments.rtm}", 
				specifications: "${discoveryTestcaseEntry.value.comments.specifications}"
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