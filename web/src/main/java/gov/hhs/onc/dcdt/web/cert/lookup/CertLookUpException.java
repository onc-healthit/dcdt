package gov.hhs.onc.dcdt.web.cert.lookup;

public class CertLookUpException extends Exception
{
	public CertLookUpException()
	{
		super();
	}

	public CertLookUpException(Throwable cause)
	{
		super(cause);
	}

	public CertLookUpException(String message)
	{
		super(message);
	}

	public CertLookUpException(String message, Throwable cause)
	{
		super(message, cause);
	}
}