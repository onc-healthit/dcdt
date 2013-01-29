<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<div class="container">
	<h2>
		${param.seldropDown}
	</h2>
</div>
<bean:define id="resultVal" name="CertLookUpActionForm" property="result" type="java.lang.String"/>
<div class="container">
	<h3><bean:write name="CertLookUpActionForm" property="result"/></h3>
</div>
<%
	String val = "Fail: Certificate found at LDAP for dts557@onctest.org";
	String dts550 = "Success: Certificate found at DNS for dts550@onctest.org";
	String dts551 = "Success: Certificate found at DNS for dts551@onctest.org";
	String dts556 = "Success: Certificate found at LDAP for dts556@onctest.org";
	String dts570 = "Success: Certificate found at LDAP for dts570@onctest.org";
%>
<logic:notEmpty name="CertLookUpActionForm" property="certResult">
	<% if (resultVal.equalsIgnoreCase(val))
	{ %>
	<!-- Nothing -->
	<%
	}
	else
	{
	%>
	<div class="container">
		<%
			if (resultVal.equalsIgnoreCase(dts550))
			{
		%>
		<h3>Verify that the discovered certificate is the intended certificate for the Direct address provided and
			verify that the rfc822Name of the SubjectAlternativeName extension is populated with the Direct address.</h3>
		<%
		}
		else if (resultVal.equalsIgnoreCase(dts551))
		{
		%>
		<h3>Verify that the discovered certificate is the intended certificate for the Direct address provided and verify that
			the dnsName extension of the SubjectAlternativeName extension is populated with the Direct Address' domain name.</h3>
		<%
		}
		else if (resultVal.equalsIgnoreCase(dts556))
		{
		%>
		<h3>Verify that the discovered certificate is the intended certificate for the Direct address provided and
			verify that the rfc822Name of the SubjectAlternativeName extension of the certificate is populated with the Direct Address.</h3>
		<%
		}
		else if (resultVal.equalsIgnoreCase(dts570))
		{
		%>
		<h3>Verify that the discovered certificate is the intended certificate for the Direct address provided and
			verify that the dnsName extension of the SubjectAlternativeName extension is populated with the Direct Address' domain name.</h3>
		<%
		}
		else
		{
		%>
		<h3>Verify that the discovered certificate is the intended certificate for the Direct address provided.</h3>
		<% } %>
		<% } %>
	</div>
	<br/>

	<div class="container">
	 <textarea name="certResults" style="width:600px" styleId="certResults" cols="200" rows="15"> 
	 	<bean:write name="CertLookUpActionForm" property="certResult"/>
	 </textarea>
	</div>
</logic:notEmpty>
<br/>
<div class="container">
	<html:link action="/hosting">Back to Hosting</html:link>
</div>