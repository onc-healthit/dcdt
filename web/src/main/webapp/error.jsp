<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="org.apache.commons.lang3.exception.ExceptionUtils"%>
<%@ page import="org.apache.struts.Globals" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%
	Throwable throwable = (Throwable)request.getAttribute(Globals.EXCEPTION_KEY);
	pageContext.setAttribute("throwable", throwable);
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/styles/error.css"/>
		<title>Direct Certificate Discovery Tool - Error</title>
	</head>
	<body>
		<h1>An Error Has Occurred</h1>
		<c:choose>
			<c:when test="${not empty throwable}">
				<pre id="errorContent"><c:if test="${not empty throwable.message}"><%= ExceptionUtils.getMessage(throwable) %></c:if><%= ExceptionUtils.getStackTrace(throwable) %></pre>
			</c:when>
			<c:otherwise>
				<html:errors/>
			</c:otherwise>
		</c:choose>
	</body>
</html>