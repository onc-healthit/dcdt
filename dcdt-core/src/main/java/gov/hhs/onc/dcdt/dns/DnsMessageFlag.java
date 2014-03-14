package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.ExtendedFlags;
import org.xbill.DNS.Flags;

/**
 * @see org.xbill.DNS.Flags
 * @see org.xbill.DNS.ExtendedFlags
 */
public enum DnsMessageFlag {
    QR(Flags.QR), AA(Flags.AA), TC(Flags.TC), RD(Flags.RD), RA(Flags.RA), AD(Flags.AD), CD(Flags.CD), DO(ExtendedFlags.DO);

    private final int flag;

    private DnsMessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }
}
