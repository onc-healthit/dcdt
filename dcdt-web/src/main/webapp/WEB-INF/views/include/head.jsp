<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<spring:eval expression="T(org.apache.commons.lang3.StringUtils).LF" var="LF" scope="request"/>
<spring:message var="urlGoogleCodeProject" code="dcdt.web.url.google.code.project" scope="request"/>
<spring:url var="urlBase" value="/" scope="request"/>
<spring:url var="urlStatic" value="/static" scope="request"/>
<spring:url var="urlStaticImages" value="/static/images" scope="request"/>
<spring:url var="urlStaticScripts" value="/static/scripts" scope="request"/>
<spring:url var="urlStaticStyles" value="/static/styles" scope="request"/>
<spring:url var="urlAdmin" value="/admin" scope="request"/>
<spring:url var="urlAdminInstanceConfigGet" value="/admin/instance" scope="request"/>
<spring:url var="urlAdminInstanceConfigRemove" value="/admin/instance/rm" scope="request"/>
<spring:url var="urlAdminInstanceConfigSet" value="/admin/instance/set" scope="request"/>
<spring:url var="urlAdminInstanceConfigCreds" value="/admin/instance/creds" scope="request"/>
<spring:url var="urlAdminLogin" value="/admin/login" scope="request"/>
<spring:url var="urlAdminLoginProcess" value="/admin/login/process" scope="request"/>
<spring:url var="urlAdminLogout" value="/admin/logout" scope="request"/>
<spring:url var="urlAdminServiceHubGet" value="/admin/service/hub" scope="request"/>
<spring:url var="urlError" value="/error" scope="request"/>
<spring:url var="urlVersion" value="/version" scope="request"/>
<c:set var="urlHome" value="${urlBase}" scope="request"/>
<spring:url var="urlHosting" value="/hosting" scope="request"/>
<spring:url var="urlHostingProcess" value="/hosting/process" scope="request"/>
<spring:url var="urlDiscovery" value="/discovery" scope="request"/>
<spring:url var="urlDiscoveryAnchor" value="/discovery/anchor" scope="request"/>
<spring:url var="urlDiscoveryMailMapping" value="/discovery/mail/mapping" scope="request"/>
<spring:url var="urlDiscoveryMailMappingAdd" value="/discovery/mail/mapping/add" scope="request"/>
<c:set var="templateName" scope="request"><tiles:getAsString name="name"/></c:set>
<c:set var="templateNameProp" scope="request" value="${fn:replace(templateName, '-', '.')}"/>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web-form.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web-beans.js"></script>
<c:if test="${googleAnalyticsConfig.enabled}">
    <script type="text/javascript">
    var googleAnalyticsId = "${googleAnalyticsConfig.id}", googleAnalyticsUrl = "${googleAnalyticsConfig.url}";
    </script>
    <script type="text/javascript" src="${urlStaticScripts}/ga.js"></script>
</c:if>
<link rel="icon" type="image/png" href="${urlStaticImages}/dcdt-logo-16x16.png"/>
<link rel="stylesheet" type="text/css" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/web.css"/>
<title><spring:message code="dcdt.web.title.${templateNameProp}"/></title>