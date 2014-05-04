<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<c:set var="adminInstanceConfigDirBase" value="${pageContext.servletContext.getRealPath('/')}" scope="request"/>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<script type="text/javascript" src="${urlStaticScripts}/${templateName}.js"></script>
<script type="text/javascript">
var URL_ADMIN_INSTANCE_CONFIG_GET = "${urlAdminInstanceConfigGet}";
var URL_ADMIN_INSTANCE_CONFIG_RM = "${urlAdminInstanceConfigRemove}";
var URL_ADMIN_INSTANCE_CONFIG_SET = "${urlAdminInstanceConfigSet}";
var URL_ADMIN_INSTANCE_CONFIG_CREDS = "${urlAdminInstanceConfigCreds}";
var ADMIN_INSTANCE_CONFIG_DIR_BASE = "${adminInstanceConfigDirBase}";
var URL_ADMIN_SERVICE_HUB_GET = "${urlAdminServiceHubGet}";
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>