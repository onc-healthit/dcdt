package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.Rcode;

/**
 * @see org.xbill.DNS.Rcode
 */
public enum DnsMessageRcode implements DnsMnemonicIdentifier {
    NOERROR(Rcode.NOERROR), FORMERR(Rcode.FORMERR), SERVFAIL(Rcode.SERVFAIL), NXDOMAIN(Rcode.NXDOMAIN), NOTIMP(Rcode.NOTIMP), NOTIMPL(Rcode.NOTIMPL), REFUSED(
        Rcode.REFUSED), YXDOMAIN(Rcode.YXDOMAIN), YXRRSET(Rcode.YXRRSET), NXRRSET(Rcode.NXRRSET), NOTAUTH(Rcode.NOTAUTH), NOTZONE(Rcode.NOTZONE), BADVERS(
        Rcode.BADVERS), BADSIG(Rcode.BADSIG), BADKEY(Rcode.BADKEY), BADTIME(Rcode.BADTIME), BADMODE(Rcode.BADMODE);

    private final int code;
    private final String id;

    private DnsMessageRcode(int code) {
        this.code = code;
        this.id = Rcode.string(code);
    }

    public boolean isError() {
        return (this != NOERROR);
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
