<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
<script type="text/javascript" src="http://www.steamdev.com/zclip/js/jquery.zclip.js"></script>
<%--
<script type="text/javascript">
// Funtion to Run call to get drop down Action
$(document).ready(function(){
	//alert('Insider the ajax call');
	var host = (document.getElementById('requestHeader').value);
	$.ajax({
		  
	       type: "GET",
	       url: "http://"+host+"<%= request.getContextPath() %>/download/anchor",
	       //data: "dts500=" +dts500,
	       success: function(data){
	           // we have the response
	           $('#info').html(data);
	           //alert('dts500 : ' +dts500);
	       },
	       error: function(e){
	           alert('Error Please set your Configuration Properties: ' + e);
	       }
	   });
	});
</script>
--%>
<input type="hidden" name="requestHeader" id="requestHeader" value="<%= request.getHeader("Host") %>"/>