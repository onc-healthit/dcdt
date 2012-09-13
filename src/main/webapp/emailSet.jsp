<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
    <jsp:include page="/include/headerbootstrap.jsp" flush="true">
	<jsp:param name="title"
		value="Set Results Email" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Set Results Email" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool -Set Results Email Screen" />
	<jsp:param name="header" value="" />
</jsp:include>

<html>
 <!-- #BeginEditable "Bodytext" -->
<body>
<h2>Please enter your Direct Email and Results Email</h2>
         
 <div id="content">          
   
<html:form action="/Email_Set">

<div style="color:red">
      <html:errors />
</div>

<div style="padding:16px">
Direct Email (i.e. the address where the messages will come from): <br /><html:text name="EmailSetActionForm" property="directEmail" /><br /><br />
Results Email (NOT a Direct address, but where you can receive regular email): <br /> <html:text name="EmailSetActionForm" property="resultsEmail" />

</div>
<div style="padding:16px">
	<div style="float:left;padding-right:8px;">
		<html:submit styleClass="btn btn-primary" value="Submit" />
	</div>
	  <html:reset styleClass="btn btn-primary" value="Reset" />
</div> 
<div style="padding:16px">
<logic:notEmpty name="EmailSetActionForm" property="resultsMessage">
	<p style="color:green"><bean:write name="EmailSetActionForm" property="resultsMessage" /></p>
</logic:notEmpty>
</div>
</html:form>
</div>
<a href="download.jsp">Back to Discovery</a>   
</body>
<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />
</html>
