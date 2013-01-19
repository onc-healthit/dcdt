package gov.hhs.onc.dcdt.web.mail.encrypt;

import gov.hhs.onc.dcdt.web.mail.MailCryptographyException;

public class MailEncryptionException extends MailCryptographyException
{
	public MailEncryptionException()
	{
		super();
	}

	public MailEncryptionException(Throwable cause)
	{
		super(cause);
	}

	public MailEncryptionException(String message)
	{
		super(message);
	}

	public MailEncryptionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}