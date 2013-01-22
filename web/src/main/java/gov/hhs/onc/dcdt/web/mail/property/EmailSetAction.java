package gov.hhs.onc.dcdt.web.mail.property;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class EmailSetAction extends Action {
	
	private final static Logger LOGGER = Logger.getLogger(EmailSetAction.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
        throws Exception {
		
		
		EmailSetActionForm esaf = (EmailSetActionForm) form;
		EmailPropertyHandler emailPH = esaf.getEph();
		emailPH.setProperty(esaf.getDirectEmail(), esaf.getResultsEmail());

		LOGGER.info("Email Entered: " + esaf.getDirectEmail() + ", " + esaf.getResultsEmail());
		esaf.setResultsMessage("Success You have entered: " + esaf.getDirectEmail() + ", " + esaf.getResultsEmail());
		
		esaf.setDirectEmail(null);
		esaf.setResultsEmail(null);
		
		return mapping.findForward("samePage");
	}
}



