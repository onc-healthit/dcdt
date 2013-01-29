<script type="text/javascript" src="${pageContext.request.contextPath}/static/scripts/discovery.js"></script>
<script type="text/javascript">
function setTextEmail(select_ele)
{
	document.getElementById("email").innerHTML = select_ele.options[select_ele.selectedIndex].value;
	val = select_ele.options[select_ele.selectedIndex].value;
	
	if (val == 500) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts500"/>';
	}
	if (val == 501) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts501"/>';
	   }
	if (val == 502) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts502"/>';
	   }
	if (val == 505) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts505"/>';
	   }
	if (val == 506) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts506"/>';
	   }
	if (val == 507) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts507"/>';
	}
	if (val == 515) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts515"/>';
	   }
	if (val == 517) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts517"/>';
	   }
	if (val == 520) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts520"/>';
	   }
	if (val == 511) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts511"/>';
	   }
	if (val == 512) {			
		  document.getElementById("email").innerHTML = '<bean:write name="LookUpDropDownActionForm" property="dts512"/>';
	   }
}
</script>