package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;

public class KeyException extends CryptographyException {
    private final static long serialVersionUID = 0L;

    public KeyException() {
        super();
    }

    public KeyException(Throwable cause) {
        super(cause);
    }

    public KeyException(String message) {
        super(message);
    }

    public KeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
