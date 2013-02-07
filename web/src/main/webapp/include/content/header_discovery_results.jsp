<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'testcases.js')}"></script>
<script type="text/javascript" src="${dcdt:scriptsPath(pageContext, 'discovery.js')}"></script>
<script type="text/javascript">
function resultsQuery()
{
	$.getJSON("${pageContext.request.contextPath}/discovery/results/query", function (data)
	{
		var resultsHeader = $("#resultsHeader"), content = $("#content"), 
			resultsNone = $("#resultsNone");
		
		resultsHeader.empty();
		content.remove("h3", "ul");
		
		if (!data.hasOwnProperty("directMailAddr"))
		{
			resultsHeader.hide();
			resultsNone.show();
			
			return;
		}
		
		resultsHeader.append("Discovery Results: ", data.directMailAddr);
		resultsHeader.show();
		
		resultsNone.hide();
		
		$(data.results).each(function (index, result)
		{
			content.append(
				$('<h3/>').append("Result ", (index + 1)), 
				$('<ul/>').append(
					$('<li/>').append($('<span/>', { "class": "resultItemLabel" }).append("From"), ": ", result.from), 
					$('<li/>').append($('<span/>', { "class": "resultItemLabel" }).append("To"), ": ", result.to), 
					$('<li/>').append($('<span/>', { "class": "resultItemLabel" }).append("Status"), ": ", result.status.name), 
					$('<li/>').append($('<span/>', { "class": "resultItemLabel" }).append("Message"), ": ", result.msg)));
		});
	});
}

$(document).ready(function ()
{
	resultsQuery();
	
	window.resultsQueryInterval = window.setInterval(resultsQuery, 30000);
});
</script>