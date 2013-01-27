package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.ToolException;

public class UtilityException extends ToolException
{
	public UtilityException()
	{
		super();
	}

	public UtilityException(Throwable cause)
	{
		super(cause);
	}

	public UtilityException(String message)
	{
		super(message);
	}

	public UtilityException(String message, Throwable cause)
	{
		super(message, cause);
	}
}