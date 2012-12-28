package gov.hhs.onc.dcdt.utils.entry;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class EntryException extends UtilityException
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