//Copy the Email Address:
$(document).ready(function ()
{
	$("#copyAddress").click(function ()
	{
		//gets the items without any seperater
		var copyAdd = $('#selectEmail option:selected').text();
		document.getElementById("seldropDown").value = copyAdd;
		alert("selected value " + copyAdd);

	});
});

//Function to set the drop down
function setDropText(select_ele)
{
	// Ignore "None"
	if (select_ele.selectedIndex == 0)
	{
		return;
	}
	document.getElementById("seldropDown").innerHTML = select_ele.options[select_ele.selectedIndex].text;

}

function getDiscoveryTestcaseHtml(label, value)
{
	return value ? '<span style="font-weight: bold">' + label + '</span>:<br/>' + value + '<br/>' : "";
}

function setText(select_ele)
{
	document.getElementById("comments").innerHTML = select_ele.options[select_ele.selectedIndex].value;
	var discoveryTestcaseId = select_ele.options[select_ele.selectedIndex].value;
	var discoveryTestcase = window.DISCOVERY_TESTCASES.hasOwnProperty(discoveryTestcaseId) ? 
		window.DISCOVERY_TESTCASES[discoveryTestcaseId] : null;
	
	if (discoveryTestcase)
	{
		var discoveryTestcaseComments = discoveryTestcase.comments;
		
		document.getElementById("comments").innerHTML = 
			getDiscoveryTestcaseHtml("Purpose/Description", discoveryTestcaseComments.description) + 
			getDiscoveryTestcaseHtml("Additional Info", discoveryTestcaseComments.additionalInfo) + 
			getDiscoveryTestcaseHtml("Target Certificate", discoveryTestcaseComments.targetCert) + 
			getDiscoveryTestcaseHtml("Background Certificates", discoveryTestcaseComments.backgroundCerts) + 
			getDiscoveryTestcaseHtml("RTM", discoveryTestcaseComments.rtm) + 
			getDiscoveryTestcaseHtml("Underlying Specification Reference", discoveryTestcaseComments.specifications);
	}
}
