package gov.hhs.onc.dcdt.net;

public class ToolUriException extends ToolNetException {
    private final static long serialVersionUID = 0L;

    public ToolUriException() {
        super();
    }

    public ToolUriException(String msg) {
        super(msg);
    }

    public ToolUriException(Throwable cause) {
        super(cause);
    }

    public ToolUriException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
