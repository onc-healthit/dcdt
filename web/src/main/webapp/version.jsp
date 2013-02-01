<%@ page import="gov.hhs.onc.dcdt.web.startup.ConfigInfo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%
	pageContext.setAttribute("moduleVersions", ConfigInfo.getConfig().getModuleVersions());
%>
<div class="container">
	<h2>Detailed Version Information</h2>
	<c:forEach var="moduleName" varStatus="moduleVersionsStatus" items="${moduleVersions.keySet()}">
		<c:set var="moduleVersion" value="${moduleVersions[moduleName]}"/>
		
		<c:if test="${not moduleVersionsStatus.first}">
			<br/>
		</c:if>
		
		<h3>Module: ${moduleName}</h3>
		<h4>Project</h4>
		<ul>
			<li>
				<span class="versionPartLabel">groupId</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.groupId}">
						${moduleVersion.groupId}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">artifactId</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.artifactId}">
						${moduleVersion.artifactId}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">version</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.version}">
						${moduleVersion.version}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">name</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.name}">
						${moduleVersion.name}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">description</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.description}">
						${moduleVersion.description}
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
					<c:when test="${not empty moduleVersion.buildTimestamp}">
						${moduleVersion.buildTimestamp}
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
					<c:when test="${not empty moduleVersion.svnAuthor}">
						${moduleVersion.svnAuthor}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">date</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.svnDate}">
						${moduleVersion.svnDate}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">url</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.svnHeadUrl}">
						${moduleVersion.svnHeadUrl}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
			<li>
				<span class="versionPartLabel">revision</span>:
				<c:choose>
					<c:when test="${not empty moduleVersion.svnRevision}">
						${moduleVersion.svnRevision}
					</c:when>
					<c:otherwise>
						<span class="versionPartUnknown">unknown</span>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
	</c:forEach>
</div>