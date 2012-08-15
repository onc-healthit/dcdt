<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
 <html>  
   <jsp:include page="/include/headerbootstrap.jsp" flush="true">
	<jsp:param name="title"
		value="DNS Look Up" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - DNS Look Up" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool -DNS Look Up Screen" />
	<jsp:param name="header" value="" />
</jsp:include>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="javascripts/dnsPage.js"></script>
<script type="text/javascript" src="javascripts/bootstrap-button.js"></script>



 <!-- #BeginEditable "Bodytext" -->
<body>
<h2>Certificate Look Up</h2>
<p> Please choose a test case from the dropdown based on your system's current capabilities. For 
example, if your system stores domain-bound certificates in DNS CERT Resource Records, you would run
DTS551. Once you've chosen a test case, read the Purpose/Description and follow the included instructions.</p>

  <div id="content">          
    <div style="color:red">
      <html:errors />
    </div>

<html:form action="/DNS_TEST">
 
<div style="padding:20px">
 
<bean:message key="label.common.html.select.testchoose" /> : 
	<html:select property="testcase" styleId="testcase"  onchange="setText(this)">
    <html:option value="">-- None --</html:option>
	<html:option value="1">DTS 550 - DNS Address-bound Certificate Search</html:option>

	<html:option value="2">DTS 551 - DNS Domain-bound Certificate Search</html:option>

	<html:option value="3">DTS 573 - DNS Address-bound Certificate Search - Case Mismatch </html:option>

	<html:option value="4">DTS 556 - LDAP Address-bound Certificate Search</html:option>

	<html:option value="5">DTS 557 - LDAP Address-bound Certificate Search - Case Mismatch</html:option>

	<html:option value="6">DTS 570 - LDAP Domain-bound Certificate Search</html:option>

	<html:option value="7">DTS 577 - LDAP - No Results Test </html:option> 
    </html:select>
<br /><br />

<div class="textFont" id="comments"></div>
<br />
<bean:message key="label.common.html.text.email" /><html:text name="CertLookUpActionForm" property="domainAddr" />
</div>
<div style="padding:16px">
	<div style="float:left;padding-right:8px;">
		<html:submit styleId = "submit"  styleClass="btn btn-primary" onclick="submitPatiently()">
                   <bean:message key="label.common.html.select.button.submit" />
                </html:submit>
	</div>
	<html:reset styleClass="btn btn-primary">	 	
           <bean:message   key="label.common.html.select.button.reset" />
        </html:reset>
        
<br />

 
<input type="hidden" id="seldropDown" name="seldropDown"/> 
</html:form> 
       </div>
</body>

<!-- #EndEditable "Bodytext" -->

<jsp:include page="/include/footer.jsp" flush="true" />
</html>