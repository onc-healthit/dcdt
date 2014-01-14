package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyException;

public class CertificateException extends CryptographyException {
    private final static long serialVersionUID = 0L;

    public CertificateException() {
        super();
    }

    public CertificateException(Throwable cause) {
        super(cause);
    }

    public CertificateException(String message) {
        super(message);
    }

    public CertificateException(String message, Throwable cause) {
        super(message, cause);
    }
}
