package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.ToolException;

public class ToolLdapException extends ToolException
{
	public ToolLdapException()
	{
		super();
	}

	public ToolLdapException(Throwable cause)
	{
		super(cause);
	}

	public ToolLdapException(String message)
	{
		super(message);
	}

	public ToolLdapException(String message, Throwable cause)
	{
		super(message, cause);
	}
}