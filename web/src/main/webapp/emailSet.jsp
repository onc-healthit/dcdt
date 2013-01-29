<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@include file="include/headerbootstrap.jsp" %>
<script type="text/javascript">
$(document).ready(function ()
{
	$("#home").removeClass("active");
});

$(document).ready(function ()
{
	$("#hosting").removeClass("active");
});


$(document).ready(function ()
{
	$("#discovery").addClass("active");
});
</script>
<div class="container">
	<h2>Please enter your Direct Email and Results Email</h2>

	<div id="content">
		<html:form action="discovery/email">
			<div style="color:red">
				<html:errors/>
			</div>
			<div style="padding:16px">
				Direct Email (i.e. the address where the messages will come from): <br/><html:text name="EmailSetActionForm" property="directEmail"/><br/><br/>
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
</body>
<%@include file="include/footer.jsp" %>
</html>
