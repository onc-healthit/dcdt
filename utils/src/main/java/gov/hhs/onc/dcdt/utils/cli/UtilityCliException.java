package gov.hhs.onc.dcdt.utils.cli;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class UtilityCliException extends UtilityException
{
	public UtilityCliException()
	{
		super();
	}

	public UtilityCliException(Throwable cause)
	{
		super(cause);
	}

	public UtilityCliException(String message)
	{
		super(message);
	}

	public UtilityCliException(String message, Throwable cause)
	{
		super(message, cause);
	}
}