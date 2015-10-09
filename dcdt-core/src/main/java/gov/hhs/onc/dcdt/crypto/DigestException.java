package gov.hhs.onc.dcdt.crypto;

public class DigestException extends CryptographyException {
    private final static long serialVersionUID = 0L;

    public DigestException() {
        super();
    }

    public DigestException(Throwable cause) {
        super(cause);
    }

    public DigestException(String message) {
        super(message);
    }

    public DigestException(String message, Throwable cause) {
        super(message, cause);
    }
}
