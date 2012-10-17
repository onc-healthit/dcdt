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
    <script type="text/javascript" src="dropdown/jquery.js"></script>
    <script type="text/javascript" src="dropdown/bootstrap-dropdown.js"></script>
    <script type="text/javascript" src="javascripts/downloadPage.js"></script>

</head>
<body>
<script type="text/javascript">
$(document).ready( function(){
    $("#home").removeClass("active");
 });

$(document).ready( function(){
    $("#hosting").removeClass("active");
 });


 $(document).ready( function(){
    $("#discovery").addClass("active");
 });

	 function setTextEmail(select_ele){
		    document.getElementById("email").innerHTML = select_ele.options[select_ele.selectedIndex].value;
		    val = select_ele.options[select_ele.selectedIndex].value;
		    
		     if (val == 500) {			
		            document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts500"/>';
		     }
		     if (val == 501) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts501"/>';
			     }
		     if (val == 502) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts502"/>';
			     }
		     if (val == 505) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts505"/>';
			     }
		     if (val == 506) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts506"/>';
			     }
		     if (val == 507) {			
		            document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts507"/>';
		     }
		     if (val == 515) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts515"/>';
			     }
		     if (val == 517) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts517"/>';
			     }
		     if (val == 520) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts520"/>';
			     }
		     if (val == 511) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts511"/>';
			     }
		     if (val == 512) {			
			        document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts512"/>';
			     }
	 }

	 
 </script>

<!-- #BeginEditable "Bodytext" -->

<div class="container">
<h2>Discover Testing Tool's Certificate</h2>

  <br />
  <p>
    <strong>Step 1: </strong>Download the Testing Tool's trust anchor.<br />
           <ul><li><a href="download/root_ca.der" target="_blank"><strong>Download Anchor</strong></a></li></ul> 
  </p>
  <p>
  <strong>Step 2: </strong> Upload the anchor to your Direct instance.  This will allow you to send messages to our tool.
  </p>
  <p>
     <strong>Step 3: </strong> Using the link below, map the Direct email address from which you will be sending messages to a non-Direct email address that will receive these messages. This email address should be able to receive plain text messages. Make sure you have access to the recipient email address in order to verify the receipt of the messages. <br />
       <ul>
       <li><a href="emailSet.jsp"><strong>Map Email Addresses</strong></a></li>
       </ul>
  </p>
   
   <strong>Step 4: </strong> Choose a test case from the drop down menu below. Test cases are named after the Direct email address that they use for test. Read the test case description below the "Chose a Direct Address" field, copy the displayed Direct address and proceed to step 5.<br />
       <div id="note" class="medFont">
     <br /><br />  
     </div>
    Choose a Direct Address:
   <select name="selectEmail" id="selectEmail" size="1" onchange="setText(this);setTextEmail(this)">
    <option value="">-- No Test Case Selected --</option>
    <option value="500">1</option>
    <option value="501">2</option>
    <option value="502">3</option>
    <option value="505">4</option>
    <option value="506">5</option>
    <option value="507">6</option>
    <option value="515">7</option>
    <option value="517">8</option>
    <option value="520">9</option>
    <option value="511">10</option>
    <option value="512">11</option>
    </select>  
    <!--  <input type="button"  id="copyAddress" value="Copy Address" class="btn-primary" onclick="copyAddress()" /> -->
  </p>
 <div class="textEmail"  id="email"></div>
 <div class="textFont" id="comments"></div>
<br /> 
    <strong>Step 5: </strong> Attempt to send a message to the Direct address that you've just copied. Please only send to one address at a time. You will receive a response to the results email telling you if you have passed the test or not. You should run all of the tests in order to verify that your system can correctly discover certificates in either DNS CERT records or LDAP servers. (Note: your system MUST NOT already contain a certificate for the address selected or the test case will not be valid).<br />
      </div>
</body>

<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />
</html>
