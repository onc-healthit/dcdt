package gov.hhs.onc.dcdt;

public class ToolException extends Exception {
    private final static long serialVersionUID = 0L;

    public ToolException() {
        super();
    }

    public ToolException(String msg) {
        super(msg);
    }

    public ToolException(Throwable cause) {
        super(cause);
    }

    public ToolException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public boolean hasCause() {
        return (this.getCause() != null);
    }
}
