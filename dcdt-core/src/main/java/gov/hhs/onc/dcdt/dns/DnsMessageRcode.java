package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Rcode;

/**
 * @see org.xbill.DNS.Rcode
 */
public enum DnsMessageRcode {
    NOERROR(Rcode.NOERROR), FORMERR(Rcode.FORMERR), SERVFAIL(Rcode.SERVFAIL), NXDOMAIN(Rcode.NXDOMAIN), NOTIMP(Rcode.NOTIMP), NOTIMPL(Rcode.NOTIMPL), REFUSED(
        Rcode.REFUSED), YXDOMAIN(Rcode.YXDOMAIN), YXRRSET(Rcode.YXRRSET), NXRRSET(Rcode.NXRRSET), NOTAUTH(Rcode.NOTAUTH), NOTZONE(Rcode.NOTZONE), BADVERS(
        Rcode.BADVERS), BADSIG(Rcode.BADSIG), BADKEY(Rcode.BADKEY), BADTIME(Rcode.BADTIME), BADMODE(Rcode.BADMODE);

    private final int rcode;

    private DnsMessageRcode(int rcode) {
        this.rcode = rcode;
    }

    public boolean isError() {
        return this != NOERROR;
    }

    public int getRcode() {
        return this.rcode;
    }
}
