package gov.hhs.onc.dcdt.utils.certgen;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class CertGenException extends UtilityException
{
	public CertGenException()
	{
		super();
	}

	public CertGenException(Throwable cause)
	{
		super(cause);
	}

	public CertGenException(String message)
	{
		super(message);
	}

	public CertGenException(String message, Throwable cause)
	{
		super(message, cause);
	}
}