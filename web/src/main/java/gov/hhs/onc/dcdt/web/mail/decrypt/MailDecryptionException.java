package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.web.mail.MailCryptographyException;

public class MailDecryptionException extends MailCryptographyException
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