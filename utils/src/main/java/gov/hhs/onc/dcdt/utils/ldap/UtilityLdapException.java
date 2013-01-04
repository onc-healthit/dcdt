package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class UtilityLdapException extends UtilityException
{
	public UtilityLdapException()
	{
		super();
	}

	public UtilityLdapException(Throwable cause)
	{
		super(cause);
	}

	public UtilityLdapException(String message)
	{
		super(message);
	}

	public UtilityLdapException(String message, Throwable cause)
	{
		super(message, cause);
	}
}