package gov.hhs.onc.dcdt.service;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class ToolServiceRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public ToolServiceRuntimeException() {
        super();
    }

    public ToolServiceRuntimeException(String msg) {
        super(msg);
    }

    public ToolServiceRuntimeException(Throwable cause) {
        super(cause);
    }

    public ToolServiceRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
