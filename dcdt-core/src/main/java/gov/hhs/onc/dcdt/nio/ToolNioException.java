package gov.hhs.onc.dcdt.nio;

import gov.hhs.onc.dcdt.ToolException;

public class ToolNioException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolNioException() {
        super();
    }

    public ToolNioException(String msg) {
        super(msg);
    }

    public ToolNioException(Throwable cause) {
        super(cause);
    }

    public ToolNioException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
