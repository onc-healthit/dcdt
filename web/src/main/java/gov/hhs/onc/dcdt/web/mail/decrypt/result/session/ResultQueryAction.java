package gov.hhs.onc.dcdt.web.mail.decrypt.result.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultQueryAction extends Action
{
	private final static Logger LOGGER = Logger.getLogger(ResultQueryAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
		HttpServletResponse response) throws Exception
	{
		String sessionId = request.getSession().getId(), directMailAddress;
		
		if (!ResultSessionMessageHandler.isSessionRegistered(sessionId))
		{
			ResultMailCookie cookie = ResultMailCookie.getCookie(request);
			
			if (cookie != null)
			{
				directMailAddress = cookie.getValue();
				
				ResultSessionMessageHandler.registerSessionAddress(sessionId, cookie.getValue());
				
				LOGGER.debug("Registered session (id=" + sessionId + ") Direct mail address: " + directMailAddress);
			}
		}
		
		if (ResultSessionMessageHandler.isSessionRegistered(sessionId))
		{
			ResultQueryActionForm resultForm = (ResultQueryActionForm)form;
			String directMailAddr = ResultSessionMessageHandler.getSessionAddress(sessionId);
			
			resultForm.setDirectMailAddr(directMailAddr);
			resultForm.setResults(ResultSessionMessageHandler.getResults(directMailAddr));
		}
		
		return mapping.getInputForward();
	}
}