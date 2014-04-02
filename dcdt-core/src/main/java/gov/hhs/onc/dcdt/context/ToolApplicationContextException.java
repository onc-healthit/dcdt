package gov.hhs.onc.dcdt.context;

import org.springframework.context.ApplicationContextException;

public class ToolApplicationContextException extends ApplicationContextException {
    private final static long serialVersionUID = 0L;

    public ToolApplicationContextException() {
        super(null);
    }

    public ToolApplicationContextException(String msg) {
        super(msg);
    }

    public ToolApplicationContextException(Throwable cause) {
        super(null, cause);
    }

    public ToolApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
