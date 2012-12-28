package gov.hhs.onc.dcdt.utils;

public class UtilityException extends Exception
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