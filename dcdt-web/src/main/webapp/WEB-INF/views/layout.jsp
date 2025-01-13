<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/WEB-INF/tag/implicit.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<dcdt:htmlcleaner>
    <!DOCTYPE html>
    <html lang="en">
        <head>
            <tiles:insertAttribute name="head-common"/>
            <tiles:insertAttribute name="head" ignore="true"/>
        </head>
        <body>
            <div id="wrap">
                <tiles:insertAttribute name="nav-common"/>
                <tiles:insertAttribute name="nav" ignore="true"/>
                <div id="content">
                    <div class="container">
                        <tiles:insertAttribute name="content-common"/>
                        <tiles:insertAttribute name="content" ignore="true"/>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer-common"/>
            <tiles:insertAttribute name="footer" ignore="true"/>
        </body>
    </html>
</dcdt:htmlcleaner>