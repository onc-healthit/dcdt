<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<c:set var="adminLoginErrorMsg" value="${(param.error != null) ? sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message : ''}"/>
<c:set var="adminLoginHasError" value="${not empty adminLoginErrorMsg}"/>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<form name="admin-login" action="${urlAdminLoginProcess}" method="post">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors input-group-addon-errors-global${adminLoginHasError ? ' input-group-addon-active' : ''}">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Login failed</strong>:
                    <ul>
                        <li>${adminLoginErrorMsg}</li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="${adminLoginHasError ? 'has-error' : ''}">
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-login-user"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-user"/>
                        <tiles:putAttribute name="content" value="Username"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-login-user" class="input-sm form-control" name="user" type="text"/>
                </span>
            </div>
        </div>
        <div class="form-group">
            <div class="${adminLoginHasError ? 'has-error' : ''}">
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-login-pass"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-asterisk"/>
                        <tiles:putAttribute name="content" value="Password"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-login-pass" class="input-sm form-control" name="pass" type="password"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-login-process"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-log-in"/>
                    <tiles:putAttribute name="content" value="Login"/>
                </tiles:insertDefinition>
            </span>
        </div>
    </div>
</form>