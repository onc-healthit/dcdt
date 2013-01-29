<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<tiles:insert name="header"/>
		
		<tiles:importAttribute name="header_content" scope="request"/>
		<logic:notEmpty name="header_content">
			<tiles:insert name="header_content"/>
		</logic:notEmpty>
		
		<tiles:importAttribute name="title" scope="request"/>
		<logic:notEmpty name="title">
			<title><tiles:getAsString name="title"/></title>
		</logic:notEmpty>
	</head>
	<body>
		<tiles:insert name="nav">
			<tiles:put name="nav_active"><tiles:getAsString name="nav_active"/></tiles:put>
		</tiles:insert>
		
		<tiles:importAttribute name="nav_content" scope="request"/>
		<logic:notEmpty name="nav_content">
			<tiles:insert name="nav_content">
				<tiles:put name="nav_active"><tiles:getAsString name="nav_active"/></tiles:put>
			</tiles:insert>
		</logic:notEmpty>
		
		<tiles:importAttribute name="content" scope="request"/>
		<logic:notEmpty name="content">
			<tiles:insert name="content"/>
		</logic:notEmpty>
		
		<tiles:insert name="footer"/>
		
		<tiles:importAttribute name="footer_content" scope="request"/>
		<logic:notEmpty name="footer_content">
			<tiles:insert name="footer_content"/>
		</logic:notEmpty>
	</body>
</html>