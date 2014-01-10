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
    var DISCOVERY_TESTCASES = {};
    $(document).ready(function () {
        <c:forEach var="discoveryTestcase" items="${discovery}">
        var specs = [];
        <c:forEach var="specification" items="${discoveryTestcase.discoveryTestcaseDescription.specifications}">
            specs.push("${specification}");
        </c:forEach>
        var backgroundCerts = [];
        <c:forEach var="backgroundCert" items="${discoveryTestcase.discoveryTestcaseDescription.backgroundCertificates}">
        backgroundCerts.push("${backgroundCert}");
        </c:forEach>

        DISCOVERY_TESTCASES["${discoveryTestcase.name}"] = {
            name: "${discoveryTestcase.name}",
            nameDisplay: "${discoveryTestcase.nameDisplay}",
            mailAddress: "${discoveryTestcase.mailAddress}",
            testcaseDescription: {
                description: "${discoveryTestcase.discoveryTestcaseDescription.description}",
                targetCertificate: "${discoveryTestcase.discoveryTestcaseDescription.targetCertificate}",
                backgroundCertificates: backgroundCerts,
                rtm: "${discoveryTestcase.discoveryTestcaseDescription.rtm}",
                specifications: specs,
                instructions: "${discoveryTestcase.discoveryTestcaseDescription.instructions}"
            }
        };
        </c:forEach>
    });
</script>
<link rel="stylesheet" type="text/css" href="${urlStaticStyles}/${templateName}.css"/>
