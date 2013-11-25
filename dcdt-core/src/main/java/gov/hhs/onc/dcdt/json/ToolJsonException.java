package gov.hhs.onc.dcdt.json;


import gov.hhs.onc.dcdt.ToolException;

public class ToolJsonException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolJsonException() {
        super();
    }

    public ToolJsonException(String msg) {
        super(msg);
    }

    public ToolJsonException(Throwable cause) {
        super(cause);
    }

    public ToolJsonException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
