package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class LdapLoaderException extends UtilityException
{
	public LdapLoaderException()
	{
		super();
	}

	public LdapLoaderException(Throwable cause)
	{
		super(cause);
	}

	public LdapLoaderException(String message)
	{
		super(message);
	}

	public LdapLoaderException(String message, Throwable cause)
	{
		super(message, cause);
	}
}