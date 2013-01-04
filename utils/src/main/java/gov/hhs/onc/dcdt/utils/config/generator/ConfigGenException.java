package gov.hhs.onc.dcdt.utils.config.generator;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class ConfigGenException extends UtilityException
{
	public ConfigGenException()
	{
		super();
	}

	public ConfigGenException(Throwable cause)
	{
		super(cause);
	}

	public ConfigGenException(String message)
	{
		super(message);
	}

	public ConfigGenException(String message, Throwable cause)
	{
		super(message, cause);
	}
}