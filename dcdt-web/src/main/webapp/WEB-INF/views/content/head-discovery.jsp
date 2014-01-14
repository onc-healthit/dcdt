<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<spring:message var="urlAnchor" code="dcdt.web.url.anchor" scope="request"/>
<spring:message var="urlEmailMapping" code="dcdt.web.url.email.mapping" scope="request"/>
<script type="text/javascript" src="${urlStaticScripts}/${templateName}.js"></script>
<script type="text/javascript" src="${urlStaticScripts}/testcases.js"></script>
<script type="text/javascript">
var DISCOVERY_TESTCASES = {
    <c:forEach var="discoveryTestcase" varStatus="discoveryTestcasesStatus" items="${discoveryTestcases}">
        "${discoveryTestcase.name}": {
            "name": "${discoveryTestcase.name}",
            "nameDisplay": "${discoveryTestcase.nameDisplay}",
            "mailAddress": "${discoveryTestcase.mailAddress}",
            "description": {
                <c:set var="discoveryTestcaseDesc" value="${discoveryTestcase.description}"/>
                "text": "${discoveryTestcaseDesc.text}",
                "instructions": "${discoveryTestcaseDesc.instructions}",
                "rtm": "${discoveryTestcaseDesc.rtm}",
                "specifications": [
                    <c:forEach var="discoveryTestcaseSpec" varStatus="discoveryTestcaseSpecsStatus"
                        items="${discoveryTestcaseDesc.specifications}">
                        "${discoveryTestcaseSpec}"<c:if test="${not discoveryTestcaseSpecsStatus.last}">,</c:if>
                    </c:forEach>
                ],
                "backgroundCertificates": [
                    <c:forEach var="discoveryTestcaseBackgroundCert" varStatus="discoveryTestcaseBackgroundCertsStatus"
                        items="${discoveryTestcaseDesc.backgroundCertificates}">
                        "${discoveryTestcaseBackgroundCert}"<c:if test="${not discoveryTestcaseBackgroundCertsStatus.last}">,</c:if>
                    </c:forEach>
                ],
                "targetCertificate": "${discoveryTestcaseDesc.targetCertificate}"
            }
        }<c:if test="${not discoveryTestcasesStatus.last}">,</c:if>
    </c:forEach>
};
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>
