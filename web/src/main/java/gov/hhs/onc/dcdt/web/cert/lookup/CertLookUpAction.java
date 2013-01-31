package gov.hhs.onc.dcdt.web.cert.lookup;

import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import gov.hhs.onc.dcdt.beans.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.beans.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CertLookUpAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
		HttpServletResponse response) throws Exception
	{
		CertLookUpActionForm clActionForm = (CertLookUpActionForm)form;

		String domainName = clActionForm.getDomainAddr(), testcaseId = clActionForm.getTestcase();
		HostingTestcase testcase = ConfigInfo.getHostingTestcases().get(testcaseId);
		HostingTestcaseResult testcaseResultPass = (testcase != null) && testcase.hasResultsPass() ? 
			testcase.getResultsPass().get(0) : null;

		CertificateInfo certificateInfo = new CertificateInfo(testcaseId, domainName);
		TestcaseResultStatus testcaseResultStatus = certificateInfo.getStatus();

		CertLookUpController cluController = new CertLookUpController(certificateInfo);

		cluController.run();

		clActionForm.setResult(certificateInfo.getResult());
		clActionForm.setCertResult(certificateInfo.getCertOutput());

		if ((testcaseResultStatus != null) && testcaseResultStatus.isPass() && (testcaseResultPass != null))
		{
			clActionForm.setResultMsg(testcaseResultPass.getMsg());
		}

		return mapping.findForward("success");
	}
}
