<%@ page language="java"%>
<%@ page session="false"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html:html>

<jsp:include page="/include/header.jsp" flush="true">
	<jsp:param name="title"
		value="Welcome Page" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Welcome" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool - Welcome Screen" />
	<jsp:param name="header" value="" />
</jsp:include>

<!-- #BeginEditable "Bodytext" -->
<body>
<td style="height:200px;width:400px;text-align:top;">
  <div id="content">
      <p> Please Select One: <br /> 
         <a href="dns.jsp"><strong>Certificate LookUp</strong></a><br />
         <a href="emailSet.jsp"><strong>Set Results Email</strong></a><br /> 
         <a href="download.jsp"><strong>Download and Upload Trust Anchor</strong></a>         
      </p>
   </div>
</td>

</body>
<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html:html>
