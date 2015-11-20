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
<div id="nav" class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a href="${urlHome}" class="navbar-brand">
                <img src="${urlStaticImages}/dcdt-logo-32x32.png" alt="DCDT logo"/>
                <strong><spring:message code="dcdt.web.title"/></strong>
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="${(templateName == 'home') ? 'active' : ''}">
                    <tiles:insertDefinition name="component-glyph-link">
                        <tiles:putAttribute name="attrs">href="${urlHome}"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-home glyphicon-type-info"/>
                        <tiles:putAttribute name="content"><strong>Home</strong></tiles:putAttribute>
                    </tiles:insertDefinition>
                </li>
                <li class="${(templateName == 'hosting') ? 'active' : ''}">
                    <tiles:insertDefinition name="component-glyph-link">
                        <tiles:putAttribute name="attrs">href="${urlHosting}"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-cloud-download glyphicon-type-info"/>
                        <tiles:putAttribute name="content"><strong>Hosting</strong></tiles:putAttribute>
                    </tiles:insertDefinition>
                </li>
                <li class="${(templateName == 'discovery') ? 'active' : ''}">
                    <tiles:insertDefinition name="component-glyph-link">
                        <tiles:putAttribute name="attrs">href="${urlDiscovery}"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-cloud-upload glyphicon-type-info"/>
                        <tiles:putAttribute name="content"><strong>Discovery</strong></tiles:putAttribute>
                    </tiles:insertDefinition>
                </li>
            </ul>
            <ul class="nav navbar-nav pull-right">
                <li class="dropdown">
                    <tiles:insertDefinition name="component-glyph-link">
                        <tiles:putAttribute name="classes" value="dropdown-toggle"/>
                        <tiles:putAttribute name="attrs">data-toggle="dropdown" href="#"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-info-sign glyphicon-type-info"/>
                        <tiles:putAttribute name="content"><strong>About</strong><span class="caret"></span></tiles:putAttribute>
                    </tiles:insertDefinition>
                    <ul role="menu" class="dropdown-menu">
                        <li role="presentation">
                            <tiles:insertDefinition name="component-glyph-link">
                                <tiles:putAttribute name="attrs">role="menuitem" tabindex="-1" target="_blank" href="${urlProjectSite}"</tiles:putAttribute>
                                <tiles:putAttribute name="glyph-classes" value="glyphicon-wrench glyphicon-type-info"/>
                                <tiles:putAttribute name="content">Project Site</tiles:putAttribute>
                            </tiles:insertDefinition>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>