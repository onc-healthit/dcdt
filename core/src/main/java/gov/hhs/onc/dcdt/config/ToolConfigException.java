package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.ToolException;

public class ToolConfigException extends ToolException
{
	public ToolConfigException()
	{
		super();
	}

	public ToolConfigException(Throwable cause)
	{
		super(cause);
	}

	public ToolConfigException(String message)
	{
		super(message);
	}

	public ToolConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}
}