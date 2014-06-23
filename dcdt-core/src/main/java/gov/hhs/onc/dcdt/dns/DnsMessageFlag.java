package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.ExtendedFlags;
import org.xbill.DNS.Flags;

/**
 * @see org.xbill.DNS.Flags
 * @see org.xbill.DNS.ExtendedFlags
 */
public enum DnsMessageFlag implements DnsMnemonicIdentifier {
    QR(Flags.QR), AA(Flags.AA), TC(Flags.TC), RD(Flags.RD), RA(Flags.RA), AD(Flags.AD), CD(Flags.CD), DO(ExtendedFlags.DO, true);

    private final int code;
    private final boolean ext;
    private final String id;

    private DnsMessageFlag(int code) {
        this(code, false);
    }

    private DnsMessageFlag(int code, boolean ext) {
        this.code = code;
        this.id = (!(this.ext = ext) ? Flags.string(code) : ExtendedFlags.string(code));
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }

    public boolean isExtended() {
        return this.ext;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
