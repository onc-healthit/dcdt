package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.ToolException;

public class ToolDnsException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolDnsException() {
        super();
    }

    public ToolDnsException(String msg) {
        super(msg);
    }

    public ToolDnsException(Throwable cause) {
        super(cause);
    }

    public ToolDnsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
