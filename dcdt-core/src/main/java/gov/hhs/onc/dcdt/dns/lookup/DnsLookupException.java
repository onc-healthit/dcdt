package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.dns.DnsException;

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
