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
<h2>Purpose of this Tool</h2>
<p>
    The Direct Certificate Discovery Tool (DCDT) was created to support automated testing of systems that plan to enact the Certificate 
    Discovery and Provider Directory Implementation Guide, approved as normative specification by the Direct community, as of July 9, 2012.
    It is based on the written test package and requirement traceability matrix created by the Modular Specifications project under the 
    direction of the Office of the National Coordinator (ONC) and National Institute of Standards and Technology (NIST).
</p>
<h2>Future Plans</h2>
<p>
    The tool fulfills Meaningful Use Stage 2 (MU2) and will be rolled into NIST's overall testing toolkit over time.
    Feedback from community usage will be prioritized as received, and the tool will have additional releases scheduled as needed.
</p>
<h2>How to Use this Tool</h2>
<p>
    Our tool is divided into two main testing areas:
</p>
<ul>
    <li>
        <a href="${urlHosting}">Hosting</a> allows a System Under Test (SUT) to verify that their certificates are hosted 
        correctly, and discoverable by other Direct implementations.
    </li>
    <li>
        <a href="${urlDiscovery}">Discovery</a> allows a SUT to verify that they can discover certificates in other Direct 
        implementations by using them to send Direct messages.
    </li>
</ul>