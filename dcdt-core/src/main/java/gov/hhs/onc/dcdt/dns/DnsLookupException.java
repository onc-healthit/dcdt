package gov.hhs.onc.dcdt.dns;

public class DnsLookupException extends DnsException {
    private final static long serialVersionUID = 0L;

    public DnsLookupException() {
        super();
    }

    public DnsLookupException(String msg) {
        super(msg);
    }

    public DnsLookupException(Throwable cause) {
        super(cause);
    }

    public DnsLookupException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
