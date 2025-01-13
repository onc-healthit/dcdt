<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<c:set var="compGlyphClassesBase" scope="page"><tiles:getAsString name="glyph-classes-base" ignore="true"/></c:set>
<c:set var="compGlyphClasses" scope="page"><tiles:getAsString name="glyph-classes" ignore="true"/></c:set>
<c:set var="compGlyphAttrsBase" scope="page"><tiles:getAsString name="glyph-attrs-base" ignore="true"/></c:set>
<c:set var="compGlyphAttrs" scope="page"><tiles:getAsString name="glyph-attrs" ignore="true"/></c:set>
<span class="${compGlyphClassesBase} ${compGlyphClasses}" ${compGlyphAttrsBase} ${compGlyphAttrs}></span>