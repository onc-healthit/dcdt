package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.ToolException;

public class CryptographyException extends ToolException {

    private static final long serialVersionUID = 0L;

    public CryptographyException() {
        super();
    }

    public CryptographyException(Throwable cause) {
        super(cause);
    }

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}
