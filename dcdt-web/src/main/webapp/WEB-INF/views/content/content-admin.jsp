<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<form name="admin-instance-config">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors input-group-addon-errors-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-instance-config-domain-name"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-link"/>
                        <tiles:putAttribute name="content" value="Domain Name"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-instance-config-domain-name" class="input-sm form-control" name="domainName" type="text"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration domain name.</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-instance-config-ip-addr"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-globe"/>
                        <tiles:putAttribute name="content" value="IP Address"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-instance-config-ip-addr" class="input-sm form-control" name="ipAddress" type="text"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration IP address.</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-instance-config-rm" disabled="disabled"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-remove-circle glyphicon-type-error"/>
                    <tiles:putAttribute name="content" value="Remove Instance Configuration"/>
                </tiles:insertDefinition>
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-instance-config-set"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-edit glyphicon-type-success"/>
                    <tiles:putAttribute name="content" value="Set Instance Configuration"/>
                </tiles:insertDefinition>
            </span>
        </div>
    </div>
</form>
