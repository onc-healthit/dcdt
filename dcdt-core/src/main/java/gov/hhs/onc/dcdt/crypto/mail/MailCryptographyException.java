package gov.hhs.onc.dcdt.crypto.mail;

import gov.hhs.onc.dcdt.ToolException;

public class MailCryptographyException extends ToolException {
    private final static long serialVersionUID = 0L;

    public MailCryptographyException() {
        super();
    }

    public MailCryptographyException(Throwable cause) {
        super(cause);
    }

    public MailCryptographyException(String message) {
        super(message);
    }

    public MailCryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}
