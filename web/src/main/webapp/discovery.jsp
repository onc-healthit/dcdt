<%@ page session="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<div class="container">
	<h2>Discover Testing Tool's Certificate</h2>
	<br/>

	<p>
		<strong>Step 1: </strong>Download the Testing Tool's trust anchor.<br/>
	<ul>
		<li><html:link action="/download/anchor" target="_blank"><strong>Download Anchor</strong></html:link></li>
	</ul>
	<p>
		<strong>Step 2: </strong> Upload the anchor to your Direct instance. This will allow you to send messages to our tool.
	</p>

	<p>
		<strong>Step 3: </strong> Using the link below, map the Direct email address from which you will be sending messages to a non-Direct email address that will receive a regular email containing test results. This email address should be able to receive plain text messages. Make sure you have access to the recipient email address in order to verify the receipt of the messages.
		<br/>
	<ul>
		<li><html:link action="/discovery/email"><strong>Map Email Addresses</strong></html:link></li>
	</ul>
	</p>
	<strong>Step 4: </strong> Choose a test case from the drop down menu below. Read the test case description below the "Choose a Direct Address" field, copy the displayed Direct address and proceed to step 5. You should run all of the tests in order to verify that your system can correctly discover certificates in either DNS CERT records or LDAP servers. (Note: your system MUST NOT already contain a certificate for
	the address selected or the test case will not be valid).<br/>

	<div id="note" class="medFont">
		<br/><br/>
	</div>
	Choose a Direct Address:
	<select name="selectEmail" id="selectEmail" size="1" onchange="setText(this); setTextEmail(this)">
		<option value="">-- No Test Case Selected --</option>
		<c:forEach var="discoveryTestcaseMail" varStatus="discoveryTestcasesStatus" items="${sessionScope.DISCOVERY_TESTCASES.keySet()}">
			<c:set var="discoveryTestcase" value="${sessionScope.DISCOVERY_TESTCASES[discoveryTestcaseMail]}"/>
			
			<option value="${discoveryTestcase.id}">${discoveryTestcase.name} - ${discoveryTestcase.comments.shortDescription}</option>
		</c:forEach>
	</select>
	<!--  <input type="button"  id="copyAddress" value="Copy Address" class="btn-primary" onclick="copyAddress()" /> -->
	</p>
	<div class="textEmail" id="email"></div>
	<div class="textFont" id="comments"></div>
	<br/>
	<strong>Step 5: </strong> Attempt to send a message to the Direct address that you've just copied.
	Please only send to one address at a time. The test case results message will indicate the test case results. If you do not receive
	a result message for test case then you should assume that the test case failed (this is not true for DTS 520, 511, and 512 - see their specific instructions in the test case details).
	<!--  You will receive a response to the results email telling you if you have passed the test or not. You should run all of the tests in order to verify that your system can correctly discover certificates in either DNS CERT records or LDAP servers. (Note: your system MUST NOT 
		already contain a certificate for the address selected or the test case will not be valid).--><br/>
</div>