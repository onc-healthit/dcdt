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
            "bindingType": "${hostingTestcase.bindingType}",
            "locationType": "${hostingTestcase.locationType}",
            <c:set var="hostingTestcaseDesc" value="${hostingTestcase.description}"/>
            "description": {
                "text": "${hostingTestcaseDesc.text}",
                "instructions": "${hostingTestcaseDesc.instructions}",
                "rtmSections": [
                    <c:forEach var="hostingTestcaseDescRtmSection" varStatus="hostingTestcaseDescRtmSectionsStatus"
                        items="${hostingTestcaseDesc.rtmSections}">
                        "${hostingTestcaseDescRtmSection}"<c:if test="${not hostingTestcaseDescRtmSectionsStatus.last}">,</c:if>
                    </c:forEach>
                ],
                "specifications": [
                    <c:forEach var="hostingTestcaseDescSpec" varStatus="hostingTestcaseDescSpecsStatus"
                        items="${hostingTestcaseDesc.specifications}">
                        "${hostingTestcaseDescSpec}"<c:if test="${not hostingTestcaseDescSpecsStatus.last}">,</c:if>
                    </c:forEach>
                ]
            }
        }<c:if test="${not hostingTestcasesStatus.last}">,</c:if>
    </c:forEach>
};
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>