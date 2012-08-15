<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<jsp:include page="/include/headerbootstrap.jsp" flush="true">
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
  <div id="content">
 <h2>
 ${param.seldropDown} </br>
 </h2> 
 
<br />
	<bean:write name="CertLookUpActionForm" property="result" />
<br />


<logic:notEmpty name="CertLookUpActionForm" property="certResult">
	<h3>Here is the discovered certificate for the Direct address that you provided:</h3>
	 <textarea name="certResults" style ="width:600px" styleId="certResults" cols="200" rows="15"> 
	 	<bean:write name="CertLookUpActionForm" property="certResult" />
	 </textarea>
	</div>
</logic:notEmpty>
<a href="dns.jsp">Back to Discovery</a>
</body>

<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html>
