<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<div class="container">
	<h2>
		${param.seldropDown}
	</h2>
</div>
<div class="container">
	<h3><bean:write name="CertLookUpActionForm" property="result"/></h3>
	<logic:notEmpty name="CertLookUpActionForm" property="resultMsg">
		<bean:write name="CertLookUpActionForm" property="resultMsg"/>
	</logic:notEmpty>
</div>
<logic:notEmpty name="CertLookUpActionForm" property="certResult">
	<div class="container">
		<textarea name="certResults" style="width: 600px" styleId="certResults" cols="200" rows="15"> 
			<bean:write name="CertLookUpActionForm" property="certResult"/>
		 </textarea>
	</div>
</logic:notEmpty>
<br/>
<div class="container">
	<html:link action="/hosting">Back to Hosting</html:link>
</div>