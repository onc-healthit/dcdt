package gov.hhs.onc.dcdt.mail;

public class ToolMailAddressException extends ToolMailException {
    private final static long serialVersionUID = 0L;

    public ToolMailAddressException() {
        super();
    }

    public ToolMailAddressException(String msg) {
        super(msg);
    }

    public ToolMailAddressException(Throwable cause) {
        super(cause);
    }

    public ToolMailAddressException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
