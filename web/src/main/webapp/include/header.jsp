<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<meta name="ROBOTS" content="none,noindex,nofollow"/>
<meta http-equiv="Content-Language" content="en-US"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="pragma" content="NO-CACHE"/>
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