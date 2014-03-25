package gov.hhs.onc.dcdt.dns;

public class DnsNameException extends DnsException {
    private final static long serialVersionUID = 0L;

    public DnsNameException() {
        super();
    }

    public DnsNameException(String msg) {
        super(msg);
    }

    public DnsNameException(Throwable cause) {
        super(cause);
    }

    public DnsNameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
