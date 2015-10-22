<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<h2>Services</h2>
<form name="admin-service-hub">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Service hub status</strong>:
                    <ul></ul>
                </div>
            </div>
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>Service hub status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-service-dns-status"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-globe"/>
                        <tiles:putAttribute name="content" value="DNS Service"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-service-dns-status" class="input-sm form-control form-control-service-status" name="serviceDnsStatus" type="text"
                        disabled="disabled"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>DNS service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>DNS service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-service-http-status"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-cloud"/>
                        <tiles:putAttribute name="content" value="HTTP Service"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-service-http-status" class="input-sm form-control form-control-service-status" name="serviceHttpStatus" type="text"
                        disabled="disabled"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>HTTP service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>HTTP service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-service-ldap-status"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-th-list"/>
                        <tiles:putAttribute name="content" value="LDAP Service"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-service-ldap-status" class="input-sm form-control form-control-service-status" name="serviceLdapStatus" type="text"
                        disabled="disabled"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>LDAP service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>LDAP service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="admin-service-mail-status"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-inbox"/>
                        <tiles:putAttribute name="content" value="Mail Service"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="admin-service-mail-status" class="input-sm form-control form-control-service-status" name="serviceMailStatus" type="text"
                        disabled="disabled"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Mail service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>Mail service status</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
    </div>
</form>
<h2>Instance Configuration</h2>
<form name="admin-instance-config" action="about:blank" method="post" target="admin-instance-config-target">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration</strong>:
                    <ul></ul>
                </div>
            </div>
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>Instance configuration modified</strong>:
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
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration domain name</strong>:
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
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid instance configuration IP address</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-instance-config-rm" disabled="disabled"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-trash glyphicon-type-error"/>
                    <tiles:putAttribute name="content" value="Remove Instance Configuration"/>
                </tiles:insertDefinition>
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-instance-config-set"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-edit glyphicon-type-success"/>
                    <tiles:putAttribute name="content" value="Set Instance Configuration"/>
                </tiles:insertDefinition>
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-instance-config-creds" disabled="disabled"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-download-alt glyphicon-type-link"/>
                    <tiles:putAttribute name="content" value="Download Instance Credentials"/>
                </tiles:insertDefinition>
            </span>
        </div>
    </div>
    <div id="dialog-admin-instance-config-rm" title="Remove Instance Configuration">
        <p>
            <tiles:insertDefinition name="component-glyph">
                <tiles:putAttribute name="glyph-classes" value="glyphicon-trash glyphicon-type-error"/>
            </tiles:insertDefinition>
            Are you sure you want to remove the current instance configuration?
        </p>
        <p>
            <strong>Note</strong>: All generated credentials (private keys + certificates), including those of the trust anchor, will be permanently deleted.
        </p>
    </div>
    <div id="dialog-admin-instance-config-set" title="Re-set Instance Configuration">
        <p>
            <tiles:insertDefinition name="component-glyph">
                <tiles:putAttribute name="glyph-classes" value="glyphicon-edit glyphicon-type-success"/>
            </tiles:insertDefinition>
            Are you sure you want to set the instance configuration again?
        </p>
        <p>
            <strong>Note</strong>: All current generated credentials (private keys + certificates), including those of the trust anchor, will be overwritten.
        </p>
    </div>
</form>
<iframe id="admin-instance-config-target" class="hide" name="admin-instance-config-target" src="about:blank"></iframe>
<h2>Mail Mappings</h2>
<form name="admin-mail-mappings" action="about:blank" method="post" target="admin-mail-mappings-target">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid mail mappings</strong>:
                    <ul></ul>
                </div>
            </div>
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>Mail mappings modified</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="admin-mail-mappings-view"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-envelope glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="View Mail Mappings"/>
                </tiles:insertDefinition>
            </span>
        </div>
    </div>
    <div id="dialog-admin-mail-mappings-view" title="View Mail Mappings">
        <div class="panel panel-default">
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Direct Address</th>
                        <th>Results Address</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</form>
<iframe id="admin-mail-mappings-target" class="hide" name="admin-mail-mappings-target" src="about:blank"></iframe>