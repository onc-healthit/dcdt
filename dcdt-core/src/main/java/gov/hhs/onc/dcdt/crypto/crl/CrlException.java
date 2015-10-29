package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyException;

public class CrlException extends CryptographyException {
    private final static long serialVersionUID = 0L;

    public CrlException() {
        super();
    }

    public CrlException(Throwable cause) {
        super(cause);
    }

    public CrlException(String message) {
        super(message);
    }

    public CrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
