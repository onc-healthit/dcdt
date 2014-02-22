package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class DnsRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public DnsRuntimeException() {
        super();
    }

    public DnsRuntimeException(String msg) {
        super(msg);
    }

    public DnsRuntimeException(Throwable cause) {
        super(cause);
    }

    public DnsRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
