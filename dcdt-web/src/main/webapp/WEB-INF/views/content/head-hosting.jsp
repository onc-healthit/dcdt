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
var HOSTING_TESTCASES = {};
$(document).ready(function () {
    <c:forEach var="hostingTestcase" items="${hosting}">
            var specs = [];
            <c:forEach var="specification" items="${hostingTestcase.hostingTestcaseDescription.specifications}">
                specs.push("${specification}");
            </c:forEach>

            HOSTING_TESTCASES["${hostingTestcase.name}"] = {
            name: "${hostingTestcase.name}",
            nameDisplay: "${hostingTestcase.nameDisplay}",
            binding: "${hostingTestcase.binding}",
            location: "${hostingTestcase.location}",
            testcaseDescription: {
                description: "${hostingTestcase.hostingTestcaseDescription.description}",
                instructions: "${hostingTestcase.hostingTestcaseDescription.instructions}",
                rtm: "${hostingTestcase.hostingTestcaseDescription.rtm}",
                specifications: specs
            }
        };
    </c:forEach>
});
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>