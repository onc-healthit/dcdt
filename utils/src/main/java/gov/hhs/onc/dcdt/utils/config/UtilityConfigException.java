package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class UtilityConfigException extends UtilityException
{
	public UtilityConfigException()
	{
		super();
	}

	public UtilityConfigException(Throwable cause)
	{
		super(cause);
	}

	public UtilityConfigException(String message)
	{
		super(message);
	}

	public UtilityConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}
}