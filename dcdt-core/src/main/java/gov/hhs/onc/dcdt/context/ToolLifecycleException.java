package gov.hhs.onc.dcdt.context;

public class ToolLifecycleException extends ToolApplicationContextException {
    private final static long serialVersionUID = 0L;

    public ToolLifecycleException() {
        super();
    }

    public ToolLifecycleException(String msg) {
        super(msg);
    }

    public ToolLifecycleException(Throwable cause) {
        super(cause);
    }

    public ToolLifecycleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
