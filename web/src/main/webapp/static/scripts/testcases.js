function appendTestcaseCommentsSection(commentsElement, label, value, asList)
{
	if (!value)
	{
		return;
	}
	
	commentsElement.append($('<span/>', { "class": "testcaseCommentsLabel" }).append(label), ":", $('<br/>'));
	
	var valueParts = asList ? value.split("|") : [ value ];
	
	var valueListElement = $('<ul/>');
	
	for (var a = 0; a < valueParts.length; a++)
	{
		valueListElement.append($('<li/>').append(valueParts[a]));
	}
	
	commentsElement.append(valueListElement);
}
