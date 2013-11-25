<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="admin-login" class="form-container">
    <spring-form:form name="admin-login-form" action="${urlAdminLoginProcess}" method="post">
        <c:if test="${param.error != null}">
            <div class="form-item has-error">
                <tiles:insertDefinition name="component-glyph">
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign glyphicon-type-error"/>
                </tiles:insertDefinition>
                <strong>Login Failed</strong>:
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:if>
        <div class="form-item">
            <span class="form-item-label-container">
                <tiles:insertDefinition name="component-glyph-label">
                    <tiles:putAttribute name="classes" value="form-item-label"/>
                    <tiles:putAttribute name="attrs">for="admin-login-user"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-user glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Username"/>
                </tiles:insertDefinition>:
            </span>
            <input id="admin-login-user" name="user" type="text"/>
        </div>
        <div class="form-item">
            <span class="form-item-label-container">
                <tiles:insertDefinition name="component-glyph-label">
                    <tiles:putAttribute name="classes" value="form-item-label"/>
                    <tiles:putAttribute name="attrs">for="admin-login-pass"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-asterisk glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Password"/>
                </tiles:insertDefinition>:
            </span>
            <input id="admin-login-pass" name="pass" type="password"/>
        </div>
        <tiles:insertDefinition name="component-glyph-button">
            <tiles:putAttribute name="attrs">id="admin-login-process"</tiles:putAttribute>
            <tiles:putAttribute name="glyph-classes" value="glyphicon-log-in glyphicon-type-info"/>
            <tiles:putAttribute name="content" value="Login"/>
        </tiles:insertDefinition>
    </spring-form:form>
</div>