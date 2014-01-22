package gov.hhs.onc.dcdt;

public class ToolRuntimeException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    public ToolRuntimeException() {
        super();
    }

    public ToolRuntimeException(String msg) {
        super(msg);
    }

    public ToolRuntimeException(Throwable cause) {
        super(cause);
    }

    public ToolRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
