<%@ page language="java"%>
<%@ page session="true"%>
<%@page import="gov.onc.decrypt.LookUpDropDownActionForm"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<head>
<jsp:include page="/include/headerbootstrap.jsp" flush="true">
	<jsp:param name="title"
		value="Upload and Download Trust Anchor" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Download and Upload" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool - Dowload and Upload screen" />
	<jsp:param name="header" value="" />
</jsp:include>

<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="http://www.steamdev.com/zclip/js/jquery.zclip.js"></script>
<script type="text/javascript" src="javascripts/jquery-latest.js"></script>
<script type="text/javascript" src="javascripts/downloadPage.js"></script>

</head>
<body>

<script type="text/javascript">
// Funtion to Run call to get drop down Action



$(document).ready(function(){
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
<h2>Discover Testing Tool's Certificate</h2>
  <div id="content">
  <br />
  <p>
    <strong>Step 1: </strong>Please use the link below to download the Testing Tool's trust anchor. Upload it to your Direct instance so that you can send messages to our tool.<br />
           <ul><li><a href="download/root_ca.der" target="_blank"><strong>Download Anchor</strong></a></li></ul> 
  </p><br />
  <p>
     <strong>Step 2: </strong> Insert the Direct address that you will be sending from and an email address that can receive
      plain text emails  (not a Direct email address).
       Please use the link below to update: <br />
       <ul>
       <li><a href="emailSet.jsp"><strong>Map Email Addresses</strong></a></li>
       </ul>
  </p>
   
   <strong>Step 3: </strong> Choose a Direct Address, read the description below the dropdown to understand what you'll be testing, copy the Direct address below and proceed to step 4.<br />
       <div id="note" class="medFont">
     <br /><br />  
     </div>
    Choose a Direct Address:
   <select name="selectEmail" id="selectEmail" size="1" onchange="setText(this);setDropText(this)">
    <option value="">-- No Test Case Selected --</option>
    <option value="500"><bean:write name="LookUpDropDownActionForm" property="dts500" /></option>
    <option value="501"><bean:write name="LookUpDropDownActionForm" property="dts501" /></option>
    <option value="502"><bean:write name="LookUpDropDownActionForm" property="dts502" /></option>
    <option value="505"><bean:write name="LookUpDropDownActionForm" property="dts505" /></option>
    <option value="506"><bean:write name="LookUpDropDownActionForm" property="dts506" /></option>
    <option value="507"><bean:write name="LookUpDropDownActionForm" property="dts507" /></option>
    <option value="515"><bean:write name="LookUpDropDownActionForm" property="dts515" /></option>
    <option value="517"><bean:write name="LookUpDropDownActionForm" property="dts517" /></option>
    <option value="520"><bean:write name="LookUpDropDownActionForm" property="dts520" /></option>
    <option value="511"><bean:write name="LookUpDropDownActionForm" property="dts511" /></option>
    <option value="512"><bean:write name="LookUpDropDownActionForm" property="dts512" /></option>
    </select>  
    <!--  <input type="button"  id="copyAddress" value="Copy Address" class="btn-primary" onclick="copyAddress()" /> -->
  </p>
 <div class="textEmail"  id="seldropDown"></div>
 <div class="textFont" id="comments"></div>
<br /> 
    <strong>Step 4: </strong> Attempt to send a message to the Direct address that you've just copied. Please only send to one address at a time. You will receive a response to the results
    	email telling you if you have passed the test or not. You should run all of the tests in order to verify that your system can correctly discover
    	certificates in either DNS CERT records or LDAP servers. (Note: your system MUST NOT already contain a certificate for the 
    	address selected or the test case will not be valid).<br />
      </div>
</body>

<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />
</html>
