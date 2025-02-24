<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<form name="form-testcases-discovery-mail-mapping" action="about:blank" method="post" target="testcases-discovery-mail-mapping-target">
    <div class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid Discovery mail mapping</strong>:
                    <ul></ul>
                </div>
            </div>
            <div class="has-success">
                <div class="input-group-addon input-group-addon-msgs input-group-addon-msgs-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-sign"/>
                    </tiles:insertDefinition>
                    <strong>Discovery mail mapping modified</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="testcase-discovery-direct-addr"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-envelope glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Direct Address"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="testcase-discovery-direct-addr" class="input-sm form-control" name="directAddress" type="text"
                        title="The Direct address you will be sending from."/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid Direct address</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="testcase-discovery-results-addr"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-inbox glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Results Address"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="testcase-discovery-results-addr" class="input-sm form-control" name="resultsAddress" type="text"
                        title="A regular (i.e. not Direct) email address you would like to receive results at."/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-msgs">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-exclamation-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid results address</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="discovery-mail-mapping-submit"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-circle glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Submit"/>
                </tiles:insertDefinition>
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="discovery-mail-mapping-reset"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-refresh glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Reset"/>
                </tiles:insertDefinition>
            </span>
        </div>
        <div class="form-group">
            <tiles:insertDefinition name="component-glyph-label">
                <tiles:putAttribute name="glyph-classes" value="glyphicon-backward glyphicon-type-info"/>
            </tiles:insertDefinition>
            <a href="${urlDiscovery}"><strong>Back to Discovery</strong></a>
        </div>
    </div>
</form>
<iframe id="testcases-discovery-mail-mapping-target" class="hide" name="testcases-discovery-mail-mapping-target" src="about:blank"></iframe>
