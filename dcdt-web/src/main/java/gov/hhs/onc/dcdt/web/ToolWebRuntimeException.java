package gov.hhs.onc.dcdt.web;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class ToolWebRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public ToolWebRuntimeException() {
        super();
    }

    public ToolWebRuntimeException(String msg) {
        super(msg);
    }

    public ToolWebRuntimeException(Throwable cause) {
        super(cause);
    }

    public ToolWebRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
