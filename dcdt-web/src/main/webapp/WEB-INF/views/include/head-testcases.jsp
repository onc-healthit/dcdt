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
<%@include file="head.jsp" %>
<c:set var="testcases" value="${requestScope[dcdt:concat(templateName, 'Testcases')]}" scope="request"/>
<script type="text/javascript" src="${urlStaticScripts}/testcases.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/${templateName}.js"></script>
<script type="text/javascript">
var TESTCASES = <dcdt:json target="${testcases}"/>;
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/testcases.css"/>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>