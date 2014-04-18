package gov.hhs.onc.dcdt.discovery;

import gov.hhs.onc.dcdt.ToolException;

public class CertificateDiscoveryException extends ToolException {
    private final static long serialVersionUID = 0L;

    public CertificateDiscoveryException() {
    }

    public CertificateDiscoveryException(String msg) {
        super(msg);
    }

    public CertificateDiscoveryException(Throwable cause) {
        super(cause);
    }

    public CertificateDiscoveryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
