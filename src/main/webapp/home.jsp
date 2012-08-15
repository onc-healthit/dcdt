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

<!-- #BeginEditable "Bodytext" -->
<body>
  <div id="content">
     <h2>Welcome to the Direct Certificate Discovery Testing Tool</h2>
     <p>This tool is intended to allow for the automated testing of Systems implementing the 
         current Certificate Discovery and Provider Directory specifications. 
     </p>
     <p>Our tool is divided into two main testing areas, one area <a href="dns.jsp">Discovery</a>
     tests that System's host their certificates appropriately for discovery by other implementations.   
     </p>
     <p>
     The other part of our tool <a href="download.jsp">Messaging</a> tests that implementations are able to
     discover valid Direct certificates and use them to send Direct messages.
     </p>
     <p>
     Both areas contain descriptions of the test cases that it is running and how to run them. If you have any questions about the tool,
     please feel free to <a href="mailto:alltest@nitorgroup.com" class="mail" >contact us</a>.
     </p>
    </div>

</body>

<a href="javascript:new_window('http://onctest.wikispaces.com')"><img  src="images/onc_image.jpg"></a>

<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html:html>
