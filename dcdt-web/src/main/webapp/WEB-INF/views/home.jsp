<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%--<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>--%>
<%@taglib prefix="x" uri="http://java.sun.com/jstl/xml" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title><spring:message code="dcdt.web.home.title"/></title>
        <%@include file="include/head.jsp"%>
        <script type="text/javascript" src="<spring:url value="/static/scripts/home.js"/>"></script>
        <link rel="stylesheet" type="text/css" href="<spring:url value="/static/styles/home.css"/>"/>
    </head>
    <body>
        <%@include file="include/nav.jsp"%>
        <div class="container">
            <h2>Welcome to the Direct Certificate Discovery Testing Tool</h2>
            <br/>
            
            <p><strong>Purpose of this Tool</strong></p>
            <p>
                The Direct Certificate Discovery Tool (DCDT) was created to support automated testing of systems that plan to enact the Certificate Discovery and Provider Directory Implementation Guide, approved as normative specification by the Direct community, as of July 9, 2012.
                It is based on the written test package and requirement traceability matrix created by the Modular Specifications project under the direction of the Office of the National Coordinator (ONC) and National Institute of Standards and Technology (NIST).
            </p>
        
            <p><strong>Future Plans</strong></p>
            <p>
                The tool fulfills Meaningful Use Stage 2 (MU2) and will be rolled into NIST's overall testing toolkit over time.
                Feedback from community usage will be prioritized as received, and the tool will have additional releases scheduled as needed.
            </p>
            
            <p><strong>How to Use this Tool</strong></p>
            Our tool is divided into two main testing areas:<br/>  
            <ul>
                <li>
                    <a href="<spring:url value="/hosting"/>">Hosting</a> allows a System Under Test (SUT) to verify that their certificates are hosted correctly, and discoverable by other Direct implementations.
                </li>
                <li>
                    <a href="<spring:url value="/discovery"/>">Discovery</a> allows a SUT to verify that they can discover certificates in other Direct implementations by using them to send Direct messages.
                </li>
            </ul>
            <p>
                Both areas contain details on the underlying test cases and how to run them.
                There is also a <a href="about:blank">User's Guide</a>.<br/>
            </p>
        </div>
    </body>
</html>