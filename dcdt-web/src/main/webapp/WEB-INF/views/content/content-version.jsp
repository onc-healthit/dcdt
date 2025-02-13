<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
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
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
            <h3>Git</h3>
            <ul>
                <li>
                    <strong>author</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitAuthor()}">
                            ${moduleVersion.gitAuthor}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>branch</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitBranch()}">
                            ${moduleVersion.gitBranch}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>commit ID</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitCommitId()}">
                            ${moduleVersion.gitCommitId}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>short commit ID</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitCommitIdShort()}">
                            ${moduleVersion.gitCommitIdShort}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>commit timestamp</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitCommitTimestampString()}">
                            ${moduleVersion.gitCommitTimestampString}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <strong>URL</strong>:
                    <c:choose>
                        <c:when test="${moduleVersion.hasGitUrl()}">
                            ${moduleVersion.gitUrl}
                        </c:when>
                        <c:otherwise>
                            <i>unknown</i>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
    </c:forEach>
</div>