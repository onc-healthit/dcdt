<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="admin-instance" class="form-container">
    <spring-form:form name="admin-instance-form">
        <div class="form-item">
            <span class="form-item-label-container">
                <tiles:insertDefinition name="component-glyph-label">
                    <tiles:putAttribute name="classes" value="form-item-label"/>
                    <tiles:putAttribute name="attrs">for="admin-instance-dir"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-folder-open glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Directory"/>
                </tiles:insertDefinition>:
            </span>
            <input id="admin-instance-dir" name="dir" type="text"/>
        </div>
        <div class="form-item">
            <span class="form-item-label-container">
                <tiles:insertDefinition name="component-glyph-label">
                    <tiles:putAttribute name="classes" value="form-item-label"/>
                    <tiles:putAttribute name="attrs">for="admin-instance-domain"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-link glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Domain"/>
                </tiles:insertDefinition>:
            </span>
            <input id="admin-instance-domain" name="domain" type="text"/>
        </div>
        <tiles:insertDefinition name="component-glyph-button">
            <tiles:putAttribute name="classes" value="admin-instance-button"/>
            <tiles:putAttribute name="attrs">id="admin-instance-rm"</tiles:putAttribute>
            <tiles:putAttribute name="glyph-classes" value="glyphicon-remove-circle glyphicon-type-error"/>
            <tiles:putAttribute name="content" value="Remove Instance Configuration"/>
        </tiles:insertDefinition>
        <tiles:insertDefinition name="component-glyph-button">
            <tiles:putAttribute name="classes" value="admin-instance-button"/>
            <tiles:putAttribute name="attrs">id="admin-instance-set"</tiles:putAttribute>
            <tiles:putAttribute name="glyph-classes" value="glyphicon-edit glyphicon-type-success"/>
            <tiles:putAttribute name="content" value="Set Instance Configuration"/>
        </tiles:insertDefinition>
    </spring-form:form>
</div>