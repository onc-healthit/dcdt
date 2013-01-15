package gov.hhs.onc.dcdt.utils.data.loader;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class DataLoaderException extends UtilityException
{
	public DataLoaderException()
	{
		super();
	}

	public DataLoaderException(Throwable cause)
	{
		super(cause);
	}

	public DataLoaderException(String message)
	{
		super(message);
	}

	public DataLoaderException(String message, Throwable cause)
	{
		super(message, cause);
	}
}