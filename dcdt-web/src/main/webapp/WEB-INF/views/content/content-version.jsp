<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="version-modules">
    <c:forEach var="moduleVersion" items="${version.moduleVersions}">
        <div class="version-module">
            <h2>Module: ${moduleVersion.artifactId}</h2>
            <h3>Project</h3>
            <ul>
                <li>
                    <strong>groupId</strong>: ${moduleVersion.groupId}
                </li>
                <li>
                    <strong>artifactId</strong>: ${moduleVersion.artifactId}
                </li>
                <li>
                    <strong>version</strong>: ${moduleVersion.version}
                </li>
                <li>
                    <strong>name</strong>: ${moduleVersion.name}
                </li>
                <li>
                    <strong>description</strong>: ${moduleVersion.description}
                </li>
            </ul>
            <h3>Build</h3>
            <ul>
                <li>
                    <strong>timestamp</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasBuildTimestampString()}">
                            ${moduleVersion.buildTimestampString}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
            <h3>Mercurial</h3>
            <ul>
                <li>
                    <strong>author</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgAuthor()}">
                            ${moduleVersion.hgAuthor}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>branch</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgBranch()}">
                            ${moduleVersion.hgBranch}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>date</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgDateString()}">
                            ${moduleVersion.hgDateString}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>node</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgNode()}">
                            ${moduleVersion.hgNode}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>nodeShort</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgNodeShort()}">
                            ${moduleVersion.hgNodeShort}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>path</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgPath()}">
                            ${moduleVersion.hgPath}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>revision</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgRevision()}">
                            ${moduleVersion.hgRevision}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>tag</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasHgTag()}">
                            ${moduleVersion.hgTag}
                        </c:when>
                        <c:otherwise>
                            <span class="version-module-attr-unknown">unknown</span>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
    </c:forEach>
</div>