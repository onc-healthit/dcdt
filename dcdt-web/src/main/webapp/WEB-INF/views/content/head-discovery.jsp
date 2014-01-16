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
            "negative": "${discoveryTestcase.negative}",
            "credentials": {
                <c:if test="${discoveryTestcase.hasBackgroundCredentials()}">
                    "background": [
                        <c:forEach var="discoveryTestcaseBackgroundCred" varStatus="discoveryTestcaseBackgroundCredsStatus"
                            items="${discoveryTestcase.backgroundCredentials}">
                            {
                                "name": "${discoveryTestcaseBackgroundCred.name}",
                                "nameDisplay": "${discoveryTestcaseBackgroundCred.nameDisplay}",
                                "description": "${discoveryTestcaseBackgroundCred.description.text}"
                            }<c:if test="${not discoveryTestcaseBackgroundCredsStatus.last}">,</c:if>
                        </c:forEach>
                    ]
                </c:if>
                <c:if test="${discoveryTestcase.hasTargetCredential()}">,
                    <c:set var="discoveryTestcaseTargetCred" value="${discoveryTestcase.targetCredential}"/>
                    "target": {
                        "name": "${discoveryTestcaseTargetCred.name}",
                        "nameDisplay": "${discoveryTestcaseTargetCred.nameDisplay}",
                        "description": "${discoveryTestcaseTargetCred.description.text}"
                    }
                </c:if>
            },
            <c:set var="discoveryTestcaseDesc" value="${discoveryTestcase.description}"/>
            "description": {
                "text": "${discoveryTestcaseDesc.text}",
                "instructions": "${discoveryTestcaseDesc.instructions}",
                "rtmSections": [
                    <c:forEach var="discoveryTestcaseDescRtmSection" varStatus="discoveryTestcaseDescRtmSectionsStatus"
                        items="${discoveryTestcaseDesc.rtmSections}">
                        "${discoveryTestcaseDescRtmSection}"<c:if test="${not discoveryTestcaseDescRtmSectionsStatus.last}">,</c:if>
                    </c:forEach>
                ],
                "specifications": [
                    <c:forEach var="discoveryTestcaseDescSpec" varStatus="discoveryTestcaseDescSpecsStatus"
                        items="${discoveryTestcaseDesc.specifications}">
                        "${discoveryTestcaseDescSpec}"<c:if test="${not discoveryTestcaseDescSpecsStatus.last}">,</c:if>
                    </c:forEach>
                ]
            }
        }<c:if test="${not discoveryTestcasesStatus.last}">,</c:if>
    </c:forEach>
};
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>
