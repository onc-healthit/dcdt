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
 <script type="text/javascript">
 <script type="text/javascript" src="dropdown/jquery.js"></script>
 <script type="text/javascript" src="dropdown/bootstrap-dropdown.js"></script>

<script type="text/javascript">

$(document).ready( function(){
    $("#home").removeClass("active");
 });

$(document).ready( function(){
    $("#discovery").removeClass("active");
 });


 $(document).ready( function(){
    $("#hosting").addClass("active");
 });
</script>
 
<!-- #BeginEditable "Bodytext" -->
<body>
<div class="container">
 	<h2>
 		${param.seldropDown} </br>
 	</h2> 
</div>

<bean:define id="resultVal" name="CertLookUpActionForm" property="result" type="java.lang.String"/>

	<div class="container">
	     <h3><bean:write name="CertLookUpActionForm" property="result" /></h3>
	</div>
    <%   
      String val = "Fail: Certificate found at LDAP for dts557@onctest.org";  
     %>  

<logic:notEmpty name="CertLookUpActionForm" property="certResult">
    	<% if (resultVal.equalsIgnoreCase(val)) { %>   	
    	  <!-- Nothing -->
    	  <%} else { %>
    	  <div class="container">
	          <h3>Verify that the discovered certificate is the intended certificate for the Direct address provided:</h3>
        <%} %>
          </div> 
	<br />
	
	<div class="container">
	 <textarea name="certResults" style ="width:600px" styleId="certResults" cols="200" rows="15"> 
	 	<bean:write name="CertLookUpActionForm" property="certResult" />
	 </textarea>
    </div>
	
</logic:notEmpty>
<br />
   <div class="container">
     <a href="dns.jsp">Back to Hosting</a>
  </div>
</body>


<!-- #EndEditable "Bodytext" -->
<jsp:include page="/include/footer.jsp" flush="true" />    
</html>

