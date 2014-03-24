package gov.hhs.onc.dcdt.crypto.mail;

import gov.hhs.onc.dcdt.mail.ToolMailException;

public class ToolMailCryptographyException extends ToolMailException {
    private final static long serialVersionUID = 0L;

    public ToolMailCryptographyException() {
        super();
    }

    public ToolMailCryptographyException(Throwable cause) {
        super(cause);
    }

    public ToolMailCryptographyException(String message) {
        super(message);
    }

    public ToolMailCryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}
