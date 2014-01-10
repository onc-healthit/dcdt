<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="discoveryIntro">
    <p>
        <strong>Step 1: </strong>Download the Testing Tool's trust anchor.<br/>
        <ul>
            <li><a href="${urlAnchor}" target="_blank"><strong>Download Anchor</strong></a></li>
        </ul><br/>
        <strong>Step 2: </strong> Upload the anchor to your Direct instance. This will allow you to send messages to our tool.
        <br/><br/>
        <strong>Step 3: </strong> Using the link below, map the Direct email address from which you will be sending messages
        to a non-Direct email address that will receive a regular email containing test results. This email address should
        be able to receive plain text messages. Make sure you have access to the recipient email address in order to verify
        the receipt of the messages.
        <br/><br/>
        <ul>
            <li><a href="${urlEmailMapping}" target="_blank"><strong>Map Email Addresses</strong></a></li>
        </ul>
        <br/>
        <strong>Step 4: </strong> Choose a test case from the drop down menu below. Read the test case description below
        the "Direct Address" field, copy the displayed Direct address and proceed to step 5. You should run all
        of the tests in order to verify that your system can correctly discover certificates in either DNS CERT records
        or LDAP servers. (Note: your system MUST NOT already contain a certificate for the address selected or the test
        case will not be valid).
        <br/><br/>
        <strong>Step 5: </strong> Attempt to send a message to the Direct address that you've just copied.
        Please only send to one address at a time. The test case results message will indicate the test case results.
        See the instructions for the test case for further information (Note: you should not receive a result test message
        for test cases D5, D6, D7, D8, D11, D12, and D13).
        <br/>
    </p>
</div>
<div id="content" class="form-container">
    <spring-form:form name="discovery-form">
        <div class="form-item">
            <span class="form-item-label-container">
                <tiles:insertDefinition name="component-glyph-label">
                    <tiles:putAttribute name="classes" value="form-item-label"/>
                    <tiles:putAttribute name="attrs">for="discoveryTestcases"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-arrow-right glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Choose test case"/>
                </tiles:insertDefinition>:
                <select id="discoveryTestcases">
                    <option value="">-- No test case selected --</option>
                    <c:forEach var="discoveryTestcase" items="${discovery}">
                        <option value=${discoveryTestcase.name}>${discoveryTestcase.nameDisplay}</option>
                    </c:forEach>
                </select>
            </span>
        </div>
        <br/>
        <div id="directAddress" class="textEmail"></div>
        <div id="discoveryTestcaseDescription"></div>
    </spring-form:form>
</div>
