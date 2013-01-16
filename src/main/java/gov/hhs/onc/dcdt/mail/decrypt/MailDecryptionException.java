package gov.hhs.onc.dcdt.mail.decrypt;

public class MailDecryptionException extends Exception
{
	public MailDecryptionException()
	{
		super();
	}

	public MailDecryptionException(Throwable cause)
	{
		super(cause);
	}

	public MailDecryptionException(String message)
	{
		super(message);
	}

	public MailDecryptionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}