package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.ToolServiceRuntimeException;

public class DnsServiceRuntimeException extends ToolServiceRuntimeException {
    private final static long serialVersionUID = 0L;

    public DnsServiceRuntimeException() {
        super();
    }

    public DnsServiceRuntimeException(String msg) {
        super(msg);
    }

    public DnsServiceRuntimeException(Throwable cause) {
        super(cause);
    }

    public DnsServiceRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
