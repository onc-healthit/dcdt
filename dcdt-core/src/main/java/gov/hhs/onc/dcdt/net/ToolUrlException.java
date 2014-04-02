package gov.hhs.onc.dcdt.net;

public class ToolUrlException extends ToolNetException {
    private final static long serialVersionUID = 0L;

    public ToolUrlException() {
        super();
    }

    public ToolUrlException(String msg) {
        super(msg);
    }

    public ToolUrlException(Throwable cause) {
        super(cause);
    }

    public ToolUrlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
