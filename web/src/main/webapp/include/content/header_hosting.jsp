<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'testcases.js')}"></script>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'hosting.js')}"></script>
<script type="text/javascript">
$(document).ready(function () {
	window.HOSTING_TESTCASES = {};
	
	<c:forEach var="hostingTestcaseEntry" varStatus="hostingTestcasesStatus" items="${dcdt:hostingTestcases()}">
		window.HOSTING_TESTCASES["${hostingTestcaseEntry.key}"] = {
			id: "${hostingTestcaseEntry.key}", 
			name: "${hostingTestcaseEntry.value.name}", 
			location: "${hostingTestcaseEntry.value.location.name}", 
			binding: "${hostingTestcaseEntry.value.binding.name}", 
			comments: {
				shortDescription: "${hostingTestcaseEntry.value.comments.shortDescription}", 
				description: "${hostingTestcaseEntry.value.comments.description}", 
				instructions: "${hostingTestcaseEntry.value.comments.instructions}", 
				rtm: "${hostingTestcaseEntry.value.comments.rtm}", 
				specifications: "${hostingTestcaseEntry.value.comments.specifications}"
			}
		};
	</c:forEach>
});
</script>