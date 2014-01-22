<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<spring:message var="urlGoogleCodeProject" code="dcdt.web.url.google.code.project" scope="request"/>
<spring:message var="urlWikiUserGuide" code="dcdt.web.url.wiki.user.guide" scope="request"/>
<spring:message var="urlWikiFaq" code="dcdt.web.url.wiki.faq" scope="request"/>
<spring:url var="urlBase" value="/" scope="request"/>
<spring:url var="urlStatic" value="/static" scope="request"/>
<spring:url var="urlStaticImages" value="/static/images" scope="request"/>
<spring:url var="urlStaticScripts" value="/static/scripts" scope="request"/>
<spring:url var="urlStaticStyles" value="/static/styles" scope="request"/>
<spring:url var="urlAdmin" value="/admin" scope="request"/>
<spring:url var="urlAdminInstanceConfigGet" value="/admin/instance" scope="request"/>
<spring:url var="urlAdminInstanceConfigRemove" value="/admin/instance/rm" scope="request"/>
<spring:url var="urlAdminInstanceConfigSet" value="/admin/instance/set" scope="request"/>
<spring:url var="urlAdminLogin" value="/admin/login" scope="request"/>
<spring:url var="urlAdminLoginProcess" value="/admin/login/process" scope="request"/>
<spring:url var="urlAdminLogout" value="/admin/logout" scope="request"/>
<spring:url var="urlError" value="/error" scope="request"/>
<spring:url var="urlVersion" value="/version" scope="request"/>
<c:set var="urlHome" value="${urlBase}" scope="request"/>
<spring:url var="urlHosting" value="/hosting" scope="request"/>
<spring:url var="urlDiscovery" value="/discovery" scope="request"/>
<spring:url var="urlDiscoveryMailMapping" value="/discovery/mail/mapping" scope="request"/>
<spring:url var="urlDiscoveryTrustAnchor" value="/discovery/anchor" scope="request"/>
<c:set var="templateName" scope="request"><tiles:getAsString name="name"/></c:set>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web-form.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/web-beans.js"></script>
<c:if test="${googleAnalytics.enabled}">
    <script type="text/javascript">
    var googleAnalyticsId = "${googleAnalytics.id}", googleAnalyticsUrl = "${googleAnalytics.url}";
    </script>
    <script type="text/javascript" src="${urlStaticScripts}/ga.js"></script>
</c:if>
<link rel="icon" type="image/png" href="${urlStaticImages}/dcdt-logo-16x16.png"/>
<link rel="stylesheet" type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css"/>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/web.css"/>
<title><spring:message code="dcdt.web.title.${templateName}"/></title>