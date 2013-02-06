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
	var commentsElement = $("#comments");
	commentsElement.empty();
	
	var hostingTestcaseId = select_ele.options[select_ele.selectedIndex].value;
	var hostingTestcase = window.HOSTING_TESTCASES.hasOwnProperty(hostingTestcaseId) ? 
		window.HOSTING_TESTCASES[hostingTestcaseId] : null;
	
	if (hostingTestcase)
	{
		var hostingTestcaseComments = hostingTestcase.comments;
		
		appendTestcaseCommentsSection(commentsElement, "Purpose/Description", hostingTestcaseComments.description, false);
		appendTestcaseCommentsSection(commentsElement, "Instructions", hostingTestcaseComments.instructions, false);
		appendTestcaseCommentsSection(commentsElement, "RTM", hostingTestcaseComments.rtm, false);
		appendTestcaseCommentsSection(commentsElement, "Underlying Specification Reference", hostingTestcaseComments.specifications, true);
	}
}
