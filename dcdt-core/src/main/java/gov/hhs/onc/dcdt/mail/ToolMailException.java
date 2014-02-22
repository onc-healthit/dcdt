package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.ToolException;

public class ToolMailException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolMailException() {
        super();
    }

    public ToolMailException(String msg) {
        super(msg);
    }

    public ToolMailException(Throwable cause) {
        super(cause);
    }

    public ToolMailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
