package gov.hhs.onc.dcdt.data;

import gov.hhs.onc.dcdt.ToolException;

public class DataException extends ToolException
{
	public DataException()
	{
		super();
	}

	public DataException(Throwable cause)
	{
		super(cause);
	}

	public DataException(String message)
	{
		super(message);
	}

	public DataException(String message, Throwable cause)
	{
		super(message, cause);
	}
}