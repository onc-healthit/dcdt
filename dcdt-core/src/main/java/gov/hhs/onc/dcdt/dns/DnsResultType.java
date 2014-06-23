package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.Lookup;

public enum DnsResultType implements DnsCodeIdentifier {
    SUCCESSFUL(Lookup.SUCCESSFUL), UNRECOVERABLE(Lookup.UNRECOVERABLE), TRY_AGAIN(Lookup.TRY_AGAIN), HOST_NOT_FOUND(Lookup.HOST_NOT_FOUND), TYPE_NOT_FOUND(
        Lookup.TYPE_NOT_FOUND);

    private final int code;

    private DnsResultType(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return (this == SUCCESSFUL);
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }
}
