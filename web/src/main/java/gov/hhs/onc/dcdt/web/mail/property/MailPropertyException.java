package gov.hhs.onc.dcdt.web.mail.property;

import gov.hhs.onc.dcdt.ToolException;

public class MailPropertyException extends ToolException
{
	public MailPropertyException()
	{
		super();
	}

	public MailPropertyException(Throwable cause)
	{
		super(cause);
	}

	public MailPropertyException(String message)
	{
		super(message);
	}

	public MailPropertyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}