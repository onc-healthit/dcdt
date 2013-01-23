package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.ToolException;

public class DnsException extends ToolException
{
	public DnsException()
	{
		super();
	}

	public DnsException(Throwable cause)
	{
		super(cause);
	}

	public DnsException(String message)
	{
		super(message);
	}

	public DnsException(String message, Throwable cause)
	{
		super(message, cause);
	}
}