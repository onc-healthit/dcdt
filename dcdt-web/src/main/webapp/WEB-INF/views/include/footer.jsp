<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<c:set var="moduleVersionDefault" value="${version.moduleVersion}" scope="request"/>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="footer">
    <div class="container">
        <hr/>
        <div id="footer-version" class="footer-content">
            <tiles:insertDefinition name="component-glyph-link">
                <tiles:putAttribute name="attrs">href="${urlVersion}"</tiles:putAttribute>
                <tiles:putAttribute name="glyph-classes" value="glyphicon-tags glyphicon-type-link"/>
                <tiles:putAttribute name="content"><strong>Version</strong></tiles:putAttribute>
            </tiles:insertDefinition>:
            ${moduleVersionDefault.version}
            (<strong>Git</strong>: ${moduleVersionDefault.gitBranch} ${moduleVersionDefault.gitCommitIdShort})
            (<strong>Build</strong>: ${moduleVersionDefault.buildTimestampString})
        </div>
        <div id="footer-admin" class="footer-content">
            <tiles:insertDefinition name="component-glyph-link">
                <tiles:putAttribute name="attrs">href="${urlAdmin}"</tiles:putAttribute>
                <tiles:putAttribute name="glyph-classes" value="glyphicon-cog glyphicon-type-link"/>
                <tiles:putAttribute name="content"><strong>Admin Console</strong></tiles:putAttribute>
            </tiles:insertDefinition>
            <c:if test="${user != null}">
                (
                <tiles:insertDefinition name="component-glyph-link">
                    <tiles:putAttribute name="attrs">href="${urlAdminLogout}"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-log-out glyphicon-type-link"/>
                    <tiles:putAttribute name="content"><strong>Logout</strong></tiles:putAttribute>
                </tiles:insertDefinition>
                )
            </c:if>
        </div>
    </div>
</div>