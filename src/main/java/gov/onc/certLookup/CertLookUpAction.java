package gov.onc.certLookup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CertLookUpAction extends Action{

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
        throws Exception {
 
	  CertLookUpActionForm clActionForm = (CertLookUpActionForm)form;
	  
	  int testCase = Integer.parseInt(clActionForm.getTestcase());
	  String domainName = clActionForm.getDomainAddr();
	  
	  CertificateInfo certificateInfo = new CertificateInfo(testCase, domainName);
	  
	  CertLookUpController cluController = new CertLookUpController(certificateInfo);
	  
	  cluController.run();
	  
	  clActionForm.setResult(certificateInfo.getResult());
	  clActionForm.setCertResult(certificateInfo.getCertOutput());	  
	  
	  return mapping.findForward("success");
	}
}
