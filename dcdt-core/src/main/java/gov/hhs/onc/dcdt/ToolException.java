package gov.hhs.onc.dcdt;

public class ToolException extends Exception {
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
}
