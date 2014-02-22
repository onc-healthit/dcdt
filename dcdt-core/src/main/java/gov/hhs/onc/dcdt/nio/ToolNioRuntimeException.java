package gov.hhs.onc.dcdt.nio;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class ToolNioRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public ToolNioRuntimeException() {
        super();
    }

    public ToolNioRuntimeException(String msg) {
        super(msg);
    }

    public ToolNioRuntimeException(Throwable cause) {
        super(cause);
    }

    public ToolNioRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
