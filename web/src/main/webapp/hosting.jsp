<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<div class="container">
	<h2>Hosting - Verify your certificate can be discovered</h2>
	<p>
		<b>Step 1:</b> Use the table below to help you determine the required test cases for your SUT (System Under Test). Notice that for the address-bound or domain-bound certificates there are two options for certificate storage. The Hosting tool provides tests that map to these options.
		<br/> <br/>
		<b>Step 2:</b> Select the option that reflects the SUT and then select the appropriate test case in the "Choose Test Case" field.
		<br/> <br/>
		<b>Step 3:</b> Read the Purpose/Description and Instructions for the selected test case. Then enter the Direct address and submit. Your SUT configuration may require that you select more than one test case. If so, then select one test case at a time, following the instructions to execute the test after each selection.
		<br/>
	</p>
	<table class="table table-striped">
		<thead>
		<tr>
			<td></td>
			<td><b>My System stores certificates in DNS CERT resource records</b></td>
			<td><b>My System stores my certificates in LDAP servers</b></td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td><b>My System stores address-bound certificates</b></td>
			<td>DTS 550</td>
			<td>DTS 556 and DTS 577</td>
		</tr>
		<tr>
			<td><b>My System stores domain-bound certificates</b></td>
			<td>DTS 551</td>
			<td>DTS 570</td>
		</tr>
		</tbody>
	</table>
	<div id="content">
		<c:if test="${pageContext.request.method eq 'POST'}">
			<html:errors/>
		</c:if>
		<html:form action="/hosting" method="post">
		<div style="padding:20px">
			<strong><bean:message key="label.common.html.select.testchoose"/> :</strong><br/>
			<html:select property="testcase" styleId="testcase" onchange="setText(this)">
				<html:option value="">-- No Test Case Selected --</html:option>
				<c:forEach var="hostingTestcaseId" varStatus="hostingTestcasesStatus" items="${sessionScope.HOSTING_TESTCASES.keySet()}">
					<c:set var="hostingTestcase" value="${sessionScope.HOSTING_TESTCASES[hostingTestcaseId]}"/>
					
					<html:option value="${hostingTestcaseId}">${hostingTestcase.name} - ${hostingTestcase.comments.shortDescription}</html:option>
				</c:forEach>
			</html:select>
			<br/><br/>

			<div class="textFont" id="comments"></div>
			<br/>
			<strong><bean:message key="label.common.html.text.email"/></strong><br/>
			<html:text name="CertLookUpActionForm" property="domainAddr"/>
		</div>
		<div style="padding:16px">
			<div style="float:left;padding-right:8px;">
				<html:submit styleId="submit" styleClass="btn btn-primary" onclick="submitPatiently()">
					<bean:message key="label.common.html.select.button.submit"/>
				</html:submit>
			</div>
			<html:reset styleClass="btn btn-primary">
				<bean:message key="label.common.html.select.button.reset"/>
			</html:reset>
			<br/>
			<input type="hidden" id="seldropDown" name="seldropDown"/>
			</html:form>
		</div>
	</div>
</div>