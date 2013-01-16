package gov.hhs.onc.dcdt.mail.property;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EmailSetActionForm extends ActionForm {

	private String directEmail;
	private String resultsEmail;
	private EmailPropertyHandler eph;
	private String resultsMessage;
	
	private final Logger log = Logger.getLogger("emailMessageLogger");
	
	

	public EmailSetActionForm() {
		super();
		eph = new EmailPropertyHandler();
		resultsMessage = "";
	}
	
	public String getResultsMessage() {
		return resultsMessage;
	}

	public void setResultsMessage(String resultsMessage) {
		this.resultsMessage = resultsMessage;
	}

	public String getDirectEmail() {
		return directEmail;
	}

	public void setDirectEmail(String directEmail) {
		this.directEmail = directEmail;
	}

	public String getResultsEmail() {
		return resultsEmail;
	}

	public void setResultsEmail(String resultsEmail) {
		this.resultsEmail = resultsEmail;
	}
	
	public EmailPropertyHandler getEph() {
		return eph;
	}

	public void setEph(EmailPropertyHandler eph) {
		this.eph = eph;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping,
	  HttpServletRequest request) {
 
	    ActionErrors errors = new ActionErrors();	    
	    try{
	    	 
	    	 
	    	 if( getDirectEmail() == null || ("".equals(getDirectEmail())))
	    	 {
	    		 errors.add("required",
	    				 new ActionMessage("invalid.direct.email.empty"));
	    	 }
	    	 else if  
	    	 (!getDirectEmail().matches("^\\S+@\\S+\\.\\S+$")){
    			 errors.add("required", new ActionMessage("invalid.direct.email.bad"));
    		 }
	    	 else{
	    		 String directDom = eph.stripDomain(getDirectEmail());
	    		 if(!eph.isEmailValid(directDom)){
	    			 errors.add("required", new ActionMessage("invalid.direct.email.bad"));
	    		 }
	    		
	    	 }
	    	
	    	 if( getResultsEmail() == null || getResultsEmail().length() < 1){
	    		 errors.add("required",
	    			new ActionMessage("invalid.result.email.empty"));
	    	 }
	    	 
	    	 else if
	    	 (!getResultsEmail().matches("^\\S+@\\S+\\.\\S+$")){
	    			errors.add("required", new ActionMessage("invalid.result.email.bad"));
	    		 }
	    	 
	    	 else{
	    		
	    		 
	    		 String resultsDom = eph.stripDomain(getResultsEmail());
	    		 if(!eph.isEmailValid(resultsDom)){
	    			 errors.add("required", new ActionMessage("invalid.result.email.bad"));
	    		 }
	    	 }
	    	
	    	 
	    }catch(Exception e){
	    	log.error("Email Update Exception Occured\n" + e.getMessage() + "\n" + e.getCause()
	    			+ "\n" + e.getStackTrace());
	    	errors.add("required", new ActionMessage("Error with email addresses."));
	    }
	    return errors;
	}
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// reset properties
		setDirectEmail("");
		setResultsEmail("");
	}
}
