<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.text.StrBuilder" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="ROBOTS" content="none,noindex,nofollow"/>
	<meta http-equiv="Content-Language" content="en-US"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="pragma" content="NO-CACHE"/>
	<title>Direct Certificate Discovery Tool</title>
	<!--<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css"/>-->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/styles/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/styles/dcdt.css"/>
	<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/images/favicon.png"/>
	<script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
	<script type="text/javascript" src="http://jquery-swip.googlecode.com/svn/trunk/jquery.popupWindow.js"></script>
	<script type="text/javascript" src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$('.oncfaq').popupWindow({
		windowURL:'http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions',
		windowName:'FAQ',
		height:500,
		width:800,
		top:50,
		left:50,
		scrollbars:1
	});
	</script>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<html:link action="/"><img src="${pageContext.request.contextPath}/static/images/dcdt_logo.png" class="brand" alt="DCDT Logo"/></html:link>
			<ul class="nav">
				<li class="active" id="home"><html:link action="/"><i class="icon-home icon-white"></i> Home</html:link></li>
				<li id="hosting"><html:link action="/hosting">Hosting</html:link></li>
				<li id="discovery"><html:link action="/discovery">Discovery</html:link></li>
			</ul>
			<ul class="nav pull-right">
				<li id="fat-menu" class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown">About <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li>
							<a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/User_Guide">
								User's Guide
							</a>
						</li>
						<li>
							<a tabindex="-1" target="_blank" href="http://www.youtube.com/watch?v=ceDlKvpvdnE&feature=youtu.be">
								Video Demo
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
								Code Repository
							</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
<br/><br/><br/><br/>
