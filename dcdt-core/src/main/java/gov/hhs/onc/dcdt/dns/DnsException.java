package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.ToolException;

public class DnsException extends ToolException {
    private final static long serialVersionUID = 0L;

    public DnsException() {
        super();
    }

    public DnsException(String msg) {
        super(msg);
    }

    public DnsException(Throwable cause) {
        super(cause);
    }

    public DnsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
