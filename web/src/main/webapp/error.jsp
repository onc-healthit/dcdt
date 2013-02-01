<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<c:set var="exceptionContent" value="${dcdt:exceptionContent(pageContext)}"/>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${dcdt:stylesPath(pageContext, 'error.css')}"/>
		<title>Direct Certificate Discovery Tool - Error</title>
	</head>
	<body>
		<h1>An Error Has Occurred</h1>
		<c:choose>
			<c:when test="${not empty exceptionContent}">
				<pre id="exceptionContent">${exceptionContent}</pre>
			</c:when>
			<c:otherwise>
				<html:errors/>
			</c:otherwise>
		</c:choose>
	</body>
</html>