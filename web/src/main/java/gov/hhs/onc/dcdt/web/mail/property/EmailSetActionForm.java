package gov.hhs.onc.dcdt.web.mail.property;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EmailSetActionForm extends ActionForm
{
	private transient final static Logger LOGGER = Logger.getLogger(EmailSetActionForm.class);
	
	private String directEmail;
	private String resultsEmail;
	private EmailPropertyHandler eph = new EmailPropertyHandler();
	private String resultsMessage = "";

	public String getResultsMessage()
	{
		return resultsMessage;
	}

	public void setResultsMessage(String resultsMessage)
	{
		this.resultsMessage = resultsMessage;
	}

	public String getDirectEmail()
	{
		return directEmail;
	}

	public void setDirectEmail(String directEmail)
	{
		this.directEmail = directEmail;
	}

	public String getResultsEmail()
	{
		return resultsEmail;
	}

	public void setResultsEmail(String resultsEmail)
	{
		this.resultsEmail = resultsEmail;
	}

	public EmailPropertyHandler getEph()
	{
		return eph;
	}

	public void setEph(EmailPropertyHandler eph)
	{
		this.eph = eph;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errors = new ActionErrors();
		try
		{

			if (getDirectEmail() == null || ("".equals(getDirectEmail())))
			{
				errors.add("required",
					new ActionMessage("invalid.direct.email.empty"));
			}
			else if
				(!getDirectEmail().matches("^\\S+@\\S+\\.\\S+$"))
			{
				errors.add("required", new ActionMessage("invalid.direct.email.bad"));
			}
			else
			{
				String directDom = eph.stripDomain(getDirectEmail());
				
				try
				{
					if (!eph.isEmailValid(directDom))
					{
						errors.add("required", new ActionMessage("invalid.direct.email.bad"));
					}
				}
				catch (MailPropertyException e)
				{
					errors.add("required", new ActionMessage("invalid.direct.email.bad.error", 
						((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage())));
				}
			}

			if (getResultsEmail() == null || getResultsEmail().length() < 1)
			{
				errors.add("required",
					new ActionMessage("invalid.result.email.empty"));
			}

			else if
				(!getResultsEmail().matches("^\\S+@\\S+\\.\\S+$"))
			{
				errors.add("required", new ActionMessage("invalid.result.email.bad"));
			}

			else
			{

				String resultsDom = eph.stripDomain(getResultsEmail());
				
				try
				{
					if (!eph.isEmailValid(resultsDom))
					{
						errors.add("required", new ActionMessage("invalid.result.email.bad"));
					}
				}
				catch (MailPropertyException e)
				{
					errors.add("required", new ActionMessage("invalid.result.email.bad.error", 
						((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage())));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Email Update Exception Occured.", e);

			errors.add("required", new ActionMessage("error.email", e.getMessage()));
		}
		
		LOGGER.trace("Found " + errors.size() + " error(s) in email addresses (direct=" + this.directEmail + ", result=" + 
			this.resultsEmail + "): [" + StringUtils.join(IteratorUtils.toArray(errors.get()), ", ") + "]");
		
		return errors;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		// reset properties
		setDirectEmail("");
		setResultsEmail("");
	}
}
