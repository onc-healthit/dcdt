<%@ page language="java"%>
<%@ page session="false"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html:html>

<jsp:include page="/include/header.jsp" flush="true">
	<jsp:param name="title"
		value="Home Page" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Home" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool - Home Screen" />
	<jsp:param name="header" value="" />
</jsp:include>

<!-- #BeginEditable "Bodytext" -->
<body>
<td style="height:200px;width:400px;text-align:top;">
  <div id="content">
     <h2>Welcome to the Direct Certificate Discovery Testing Tool</h2>
     <p>This tool is intended to allow for the automated testing of Systems implementing the 
         current Certificate Discovery and Provider Directory. 
     </p>
     <p>Our tool is divided into two main testing areas, one area <a href="/ModularSpecPhase3_Tool/dns.jsp">Discovery</a>
     tests that System's host their certificates appropriately for discovery by other implementations.   
     </p>
     <p>
     The other part of our tool <a href="/ModularSpecPhase3_Tool/download.jsp">Messaging</a> tests that implementations are able to
     discover valid Direct certificates and use them to send Direct messages.
     </p>
     <p>
     Both areas contain descriptions of the test cases that it is running and how to run them. If you have any questions about the tool,
     please feel free to contact us at <a href="mailto:alltest@nitorgroup.com" class="mail" >e-mail.</a>
     </p>
     
     
     
     
   </div>
</td>

</body>
<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html:html>
