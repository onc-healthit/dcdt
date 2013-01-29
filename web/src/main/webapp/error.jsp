<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="org.apache.struts.Globals"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%
	pageContext.setAttribute("EXCEPTION_KEY", Globals.EXCEPTION_KEY);
%>
<c:set var="exception" value="${requestScope[pageScope.EXCEPTION_KEY]}"/>
<html>
	<head>
		<title>Direct Certificate Discovery Tool - Error</title>
	</head>
	<body>
		<c:choose>
			<c:when test="${not empty exception}">
				<h1>${exception.message}</h1>
				<pre>
					<c:forEach items="${exception.stackTrace}" var="stackTraceFrame">
						<c:out value="${stackTraceFrame}"/>
					</c:forEach>
				</pre>
			</c:when>
			<c:otherwise>
				<html:errors/>
			</c:otherwise>
		</c:choose>
	</body>
</html>