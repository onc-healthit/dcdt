package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Lookup;

public enum DnsResultType {
    SUCCESSFUL(Lookup.SUCCESSFUL), UNRECOVERABLE(Lookup.UNRECOVERABLE), TRY_AGAIN(Lookup.TRY_AGAIN), HOST_NOT_FOUND(Lookup.HOST_NOT_FOUND), TYPE_NOT_FOUND(
        Lookup.TYPE_NOT_FOUND);

    private final int result;

    private DnsResultType(int result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return (this == SUCCESSFUL);
    }

    public int getResult() {
        return this.result;
    }
}
