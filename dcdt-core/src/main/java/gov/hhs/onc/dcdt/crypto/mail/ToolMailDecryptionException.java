package gov.hhs.onc.dcdt.crypto.mail;

public class ToolMailDecryptionException extends ToolMailCryptographyException {
    private final static long serialVersionUID = 0L;

    public ToolMailDecryptionException() {
        super();
    }

    public ToolMailDecryptionException(Throwable cause) {
        super(cause);
    }

    public ToolMailDecryptionException(String message) {
        super(message);
    }

    public ToolMailDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
