package gov.onc.certLookup;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class CertLookUpActionForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	
	private String testcase;
	private String domainAddr;
	private String certResult;
	private String result;
	
	public String getTestcase() {
		return testcase;
	}
	public void setTestcase(String testcase) {
		this.testcase = testcase;
	}
	public String getDomainAddr() {
		return domainAddr;
	}
	public void setDomainAddr(String domainAddr) {
		this.domainAddr = domainAddr;
	}
	
	public String getCertResult() {
		return certResult;
	}

	public void setCertResult(String certResult) {
		this.certResult = certResult;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public ActionErrors validate(ActionMapping mapping,
	  HttpServletRequest request) {
 
	    ActionErrors errors = new ActionErrors();
 
	    if( getTestcase() == null || ("".equals(getTestcase())))
	    {
	       errors.add("required",
	    	 new ActionMessage("error.common.html.select.required"));
	    }
	    
	    if( getDomainAddr() == null || getDomainAddr().length() < 1){
	    	errors.add("required",
	    			new ActionMessage("error.common.html.domain.required"));
	    }
 
	    return errors;
	}
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// reset properties
		testcase = "";
		domainAddr = "";
	}
}
