<%@ page import="gov.hhs.onc.dcdt.web.startup.ConfigInfo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="/META-INF/gov/hhs/onc/dcdt/web/tags/dcdt.tld" prefix="dcdt"%>
<div class="container">
	<h2>Detailed Version Information</h2>
	<c:forEach var="moduleVersionEntry" varStatus="moduleVersionsStatus" items="${dcdt:moduleVersions()}">
		<c:if test="${not moduleVersionsStatus.first}">
			<br/>
		</c:if>
		
		<h3>Module: ${moduleVersionEntry.key}</h3>
		<h4>Project</h4>
		<ul>
			<li>
				<span class="versionPartLabel">groupId</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.groupId}">
						${moduleVersionEntry.value.groupId}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">artifactId</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.artifactId}">
						${moduleVersionEntry.value.artifactId}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">version</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.version}">
						${moduleVersionEntry.value.version}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">name</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.name}">
						${moduleVersionEntry.value.name}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">description</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.description}">
						${moduleVersionEntry.value.description}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
		<h4>Build</h4>
		<ul>
			<li>
				<span class="versionPartLabel">timestamp</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.buildTimestamp}">
						${moduleVersionEntry.value.buildTimestamp}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
		<h4>Subversion</h4>
		<ul>
			<li>
				<span class="versionPartLabel">author</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.hgAuthor}">
						${moduleVersionEntry.value.hgAuthor}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">date</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.hgDate}">
						${moduleVersionEntry.value.hgDate}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">branch/tag</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.hgTag}">
						${moduleVersionEntry.value.hgTag}
					</c:when>
                    <c:when test="${not empty moduleVersionEntry.value.hgBranch}">
						${moduleVersionEntry.value.hgBranch}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">revision</span>:
				<c:choose>
					<c:when test="${not empty moduleVersionEntry.value.hgRevision}">
						${moduleVersionEntry.value.hgRevision}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
	</c:forEach>
</div>