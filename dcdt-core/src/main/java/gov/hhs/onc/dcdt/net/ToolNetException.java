package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.ToolException;

public class ToolNetException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolNetException() {
        super();
    }

    public ToolNetException(String msg) {
        super(msg);
    }

    public ToolNetException(Throwable cause) {
        super(cause);
    }

    public ToolNetException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
