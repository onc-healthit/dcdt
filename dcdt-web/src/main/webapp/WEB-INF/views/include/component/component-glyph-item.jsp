<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<c:set var="compTag" scope="page"><tiles:getAsString name="tag"/></c:set>
<c:set var="compClassesBase" scope="page"><tiles:getAsString name="classes-base" ignore="true"/></c:set>
<c:set var="compClasses" scope="page"><tiles:getAsString name="classes" ignore="true"/></c:set>
<c:set var="compAttrsBase" scope="page"><tiles:getAsString name="attrs-base" ignore="true"/></c:set>
<c:set var="compAttrs" scope="page"><tiles:getAsString name="attrs" ignore="true"/></c:set>
<c:set var="compContentBase" scope="page"><tiles:getAsString name="content-base" ignore="true"/></c:set>
<c:set var="compContent" scope="page"><tiles:getAsString name="content" ignore="true"/></c:set>
<c:set var="compGlyphClassesBase" scope="page"><tiles:getAsString name="glyph-classes-base" ignore="true"/></c:set>
<c:set var="compGlyphClasses" scope="page"><tiles:getAsString name="glyph-classes" ignore="true"/></c:set>
<c:set var="compGlyphAttrsBase" scope="page"><tiles:getAsString name="glyph-attrs-base" ignore="true"/></c:set>
<c:set var="compGlyphAttrs" scope="page"><tiles:getAsString name="glyph-attrs" ignore="true"/></c:set>
<${compTag} class="${compClassesBase} ${compClasses}" ${compAttrsBase} ${compAttrs}><!--
    --><tiles:insertAttribute name="glyph-def">
        <tiles:putAttribute name="glyph-classes-base" value="${compGlyphClassesBase}"/>
        <tiles:putAttribute name="glyph-classes" value="${compGlyphClasses}"/>
        <tiles:putAttribute name="glyph-attrs-base" value="${compGlyphAttrsBase}"/>
        <tiles:putAttribute name="glyph-attrs" value="${compGlyphAttrs}"/>
    </tiles:insertAttribute><!--
    -->${compContentBase}${compContent}<!--
--></${compTag}>