package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.ExtendedFlags;
import org.xbill.DNS.Flags;

/**
 * @see org.xbill.DNS.Flags
 * @see org.xbill.DNS.ExtendedFlags
 */
public enum DnsMessageFlag implements DnsMnemonicIdentifier {
    QR(Flags.QR), AA(Flags.AA), TC(Flags.TC), RD(Flags.RD), RA(Flags.RA), AD(Flags.AD), CD(Flags.CD), DO(ExtendedFlags.DO, ExtendedFlags
        .string(ExtendedFlags.DO));

    private final int code;
    private final String id;

    private DnsMessageFlag(int code) {
        this(code, Flags.string(code));
    }

    private DnsMessageFlag(int code, String id) {
        this.code = code;
        this.id = id;
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
