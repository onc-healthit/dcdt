package gov.hhs.onc.dcdt;

public class ToolException extends Exception
{
	public ToolException()
	{
		super();
	}

	public ToolException(Throwable cause)
	{
		super(cause);
	}

	public ToolException(String message)
	{
		super(message);
	}

	public ToolException(String message, Throwable cause)
	{
		super(message, cause);
	}
}