<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <div class="brand">
                <a href="<spring:url value="/"/>">
                    <img src="<spring:url value="/static/images/dcdt-logo-48x48.png"/>"/><spring:message code="dcdt.web.nav.title"/>
                </a>
            </div>
            <ul class="nav">
                <li id="home">
                    <a href="<spring:url value="/"/>"><i class="icon-home icon-white"></i> Home</a>
                </li>
                <li id="hosting">
                    <a href="<spring:url value="/hosting"/>">Hosting</a>
                </li>
                <li id="discovery">
                    <a href="<spring:url value="/discovery"/>">Discovery</a>
                </li>
            </ul>
            <ul class="nav pull-right">
                <li id="fat-menu" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown">About <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/User_Guide_2_1">
                                User's Guide
                            </a>
                        </li>
                        <li>
                            <a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions">
                                FAQ
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/">
                                Project Site
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>