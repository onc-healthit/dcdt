<%@ page contentType="application/json;charset=UTF-8" %><%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>{
<logic:notEmpty name="ResultQueryActionForm" property="results"><bean:define id="directMailAddr" name="ResultQueryActionForm" property="directMailAddr"/><bean:define id="results" name="ResultQueryActionForm" property="results"/>
	"directMailAddr": "${directMailAddr}", 
	"results": 
	[<c:forEach var="result" varStatus="resultsStatus" items="${results}"><c:if test="${not resultsStatus.first}">, </c:if>
		{
			"received": "${result.received}", 
			"from": "${result.fromAddress}", 
			"to": "${result.toAddress}", 
			"status": 
			{
				"name": "${result.resultStatus.name}", 
				"nameDisplay": "${result.resultStatus.nameDisplay}", 
				"pass": ${result.resultStatus.pass}, 
				"optional": ${result.resultStatus.optional}, 
				"fail": ${result.resultStatus.fail}
			}, 
			"msg": "${result.resultMsg}"
		}</c:forEach>
	]</logic:notEmpty>
}