package gov.hhs.onc.dcdt.crypto.mail.decrypt;

import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;

public class MailDecryptionException extends MailCryptographyException {
    private final static long serialVersionUID = 0L;

    public MailDecryptionException() {
        super();
    }

    public MailDecryptionException(Throwable cause) {
        super(cause);
    }

    public MailDecryptionException(String message) {
        super(message);
    }

    public MailDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
