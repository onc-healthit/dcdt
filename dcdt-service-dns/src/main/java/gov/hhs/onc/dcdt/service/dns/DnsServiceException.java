package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.ToolServiceException;

public class DnsServiceException extends ToolServiceException {
    private final static long serialVersionUID = 0L;

    public DnsServiceException() {
        super();
    }

    public DnsServiceException(String msg) {
        super(msg);
    }

    public DnsServiceException(Throwable cause) {
        super(cause);
    }

    public DnsServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
