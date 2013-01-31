//Function to load the button
$(document).ready(function ()
{
	$('#submit').click(function ()
	{
		var btn = $(this);
		btn.button('loading'); // call the loading function
		setTimeout(function ()
		{
			btn.button('reset'); // call the reset function
		}, 3000);
	});

	//Function to select the drop down box
	$("#submit").click(function ()
	{
		document.getElementById("seldropDown").value = $('#testcase option:selected').text();
	});
});

// Function to set the drop down values
function setText(select_ele)
{
	document.getElementById("comments").innerHTML = select_ele.options[select_ele.selectedIndex].value;
	var hostingTestcaseId = select_ele.options[select_ele.selectedIndex].value;
	var hostingTestcase = window.HOSTING_TESTCASES.hasOwnProperty(hostingTestcaseId) ? 
		window.HOSTING_TESTCASES[hostingTestcaseId] : null;
	
	if (hostingTestcase)
	{
		var hostingTestcaseComments = hostingTestcase.comments;
		
		document.getElementById("comments").innerHTML = 
			getTestcaseCommentsEntryHtml("Purpose/Description", hostingTestcaseComments.description) + 
			getTestcaseCommentsEntryHtml("Instructions", hostingTestcaseComments.instructions) + 
			getTestcaseCommentsEntryHtml("RTM", hostingTestcaseComments.rtm) + 
			getTestcaseCommentsEntryHtml("Underlying Specification Reference", hostingTestcaseComments.specifications);
	}
}
