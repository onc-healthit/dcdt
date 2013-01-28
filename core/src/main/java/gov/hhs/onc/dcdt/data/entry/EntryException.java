package gov.hhs.onc.dcdt.data.entry;

import gov.hhs.onc.dcdt.ToolException;

public class EntryException extends ToolException
{
	public EntryException()
	{
		super();
	}

	public EntryException(Throwable cause)
	{
		super(cause);
	}

	public EntryException(String message)
	{
		super(message);
	}

	public EntryException(String message, Throwable cause)
	{
		super(message, cause);
	}
}