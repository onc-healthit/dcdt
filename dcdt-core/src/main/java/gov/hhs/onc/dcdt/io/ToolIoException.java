package gov.hhs.onc.dcdt.io;

import gov.hhs.onc.dcdt.ToolException;

public class ToolIoException extends ToolException {
    private final static long serialVersionUID = 0L;
    
    public ToolIoException() {
        super();
    }

    public ToolIoException(String msg) {
        super(msg);
    }

    public ToolIoException(Throwable cause) {
        super(cause);
    }

    public ToolIoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
