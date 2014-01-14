<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<script type="text/javascript" src="${urlStaticScripts}/${templateName}.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/testcases.js"></script>
<script type="text/javascript">
var HOSTING_TESTCASES = {
    <c:forEach var="hostingTestcase" varStatus="hostingTestcasesStatus" items="${hostingTestcases}">
        "${hostingTestcase.name}": {
            "name": "${hostingTestcase.name}",
            "nameDisplay": "${hostingTestcase.nameDisplay}",
            "binding": "${hostingTestcase.binding}",
            "location": "${hostingTestcase.location}",
            "description": {
                <c:set var="hostingTestcaseDesc" value="${hostingTestcase.description}"/>
                "text": "${hostingTestcaseDesc.text}",
                "instructions": "${hostingTestcaseDesc.instructions}",
                "rtm": "${hostingTestcaseDesc.rtm}",
                "specifications": [
                    <c:forEach var="hostingTestcaseSpec" varStatus="hostingTestcaseSpecsStatus"
                        items="${hostingTestcaseDesc.specifications}">
                        "${hostingTestcaseSpec}"<c:if test="${not hostingTestcaseSpecsStatus.last}">,</c:if>
                    </c:forEach>
                ]
            }
        }<c:if test="${not hostingTestcasesStatus.last}">,</c:if>
    </c:forEach>
};
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>