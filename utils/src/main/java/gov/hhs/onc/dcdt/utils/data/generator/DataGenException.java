package gov.hhs.onc.dcdt.utils.data.generator;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class DataGenException extends UtilityException
{
	public DataGenException()
	{
		super();
	}

	public DataGenException(Throwable cause)
	{
		super(cause);
	}

	public DataGenException(String message)
	{
		super(message);
	}

	public DataGenException(String message, Throwable cause)
	{
		super(message, cause);
	}
}