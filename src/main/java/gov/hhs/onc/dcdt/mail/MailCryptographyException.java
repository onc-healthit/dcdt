package gov.hhs.onc.dcdt.mail;

public class MailCryptographyException extends Exception
{
	public MailCryptographyException()
	{
		super();
	}

	public MailCryptographyException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MailCryptographyException(String message)
	{
		super(message);
	}

	public MailCryptographyException(Throwable cause)
	{
		super(cause);
	}
}