package gov.hhs.onc.dcdt.web;


import gov.hhs.onc.dcdt.ToolException;

public class ToolWebException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolWebException() {
        super();
    }

    public ToolWebException(String msg) {
        super(msg);
    }

    public ToolWebException(Throwable cause) {
        super(cause);
    }

    public ToolWebException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
