<%@page session="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<div class="container">
	<h2>Please enter your Direct Email and Results Email</h2>
	<div id="content">
		<html:form action="discovery/email" method="post">
			<c:if test="${pageContext.request.method eq 'POST'}">
				<html:errors/>
			</c:if>
			<div style="padding:16px">
				Direct Email (i.e. the address where the messages will come from):<br/>
				<html:text name="EmailSetActionForm" property="directEmail"/><br/><br/>
				Results Email (NOT a Direct address, but where you can receive regular email): <br/>
				<html:text name="EmailSetActionForm" property="resultsEmail"/>
			</div>
			<div style="padding:16px">
				<div style="float:left;padding-right:8px;">
					<html:submit styleClass="btn btn-primary" value="Submit"/>
				</div>
				<html:reset styleClass="btn btn-primary" value="Reset"/>
			</div>
			<div style="padding:16px">
				<logic:notEmpty name="EmailSetActionForm" property="resultsMessage">
					<p style="color:green"><bean:write name="EmailSetActionForm" property="resultsMessage"/></p>
				</logic:notEmpty>
			</div>
		</html:form>
	</div>
	<html:link action="/discovery">Back to Discovery</html:link>
</div>