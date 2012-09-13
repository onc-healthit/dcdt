<%@ page language="java"%>
<%@ page session="false"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html:html>

<jsp:include page="/include/headerbootstrap.jsp" flush="true">
	<jsp:param name="title"
		value="Home Page" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Home" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool - Home Screen" />
	<jsp:param name="header" value="" />
</jsp:include>

<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="http://www.steamdev.com/zclip/js/jquery.zclip.js"></script>
<script type="text/javascript" src="javascripts/jquery-latest.js"></script>

<script type="text/javascript">
// Funtion to Run call to get drop down Action
$(document).ready(function(){
	//alert('Insider the ajax call');
	var host = (document.getElementById('requestHeader').value);
	$.ajax({
		  
	       type: "GET",
	       url: "http://"+host+"/ModularSpecPhase3_Tool/Download.do",
	       //data: "dts500=" +dts500,
	       success: function(data){
	           // we have the response
	           $('#info').html(data);
	           //alert('dts500 : ' +dts500);
	       },
	       error: function(e){
	           alert('Error: ' + e);
	       }
	   });
	});
</script>
<input type="hidden" name="requestHeader" id="requestHeader" value="<%=request.getHeader("Host")%>" />

<!-- #BeginEditable "Bodytext" -->
<body>
  <div id="content">
     <h2>Welcome to the Direct Certificate Discovery Testing Tool</h2>
     <br />
     <p><strong>Purpose of this Tool</strong></p>
     <p>This tool is intended to allow for the automated testing of Systems implementing the 
     current Certificate Discovery and Provider Directory Implementation Guidance, 
     approved by Direct community, as of July 9, 2012. </p>
     
     <p><strong>Current State and Future Plans</strong></p>
      <p>Info under Construction</p>
  
 
    <p><strong>How to Use this Tool</strong></p>
    Our tool is divided into two main testing areas, one area <a href="dns.jsp">Hosting</a>
    tests that System's host their certificates appropriately for discovery by other implementations.  
    <p>
     The other part of our tool <a href="download.jsp">Discovery</a> tests that implementations are able to
     discover valid Direct certificates and use them to send Direct messages.
    </p>
     <p>
     Both areas contain descriptions of the test cases that it is running and how to run them.   <!-- If you have any questions about the tool,
    please feel free to <a href="mailto:alltest@nitorgroup.com" class="mail" >contact us</a>.-->
     </p>
     <p><strong>Have Questions?</strong><br /></p>
     Feel free to contact us with any questions via email found at the footer of the page "Contact Us".
    </div>

</body>

<!--  <a href="javascript:new_window('http://onctest.wikispaces.com')"><img  src="images/onc_image.jpg"></a>-->

<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html:html>
