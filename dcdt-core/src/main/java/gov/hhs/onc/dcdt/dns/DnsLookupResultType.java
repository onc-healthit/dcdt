package gov.hhs.onc.dcdt.dns;

import java.util.EnumSet;
import javax.annotation.Nullable;
import org.xbill.DNS.Lookup;

public enum DnsLookupResultType {
    UKNOWN(-1, "unknown"), SUCCESSFUL(Lookup.SUCCESSFUL, "successful"), UNRECOVERABLE(Lookup.UNRECOVERABLE, "unrecoverable"), TRY_AGAIN(Lookup.TRY_AGAIN,
        "try_again"), HOST_NOT_FOUND(Lookup.HOST_NOT_FOUND, "host_not_found"), TYPE_NOT_FOUND(Lookup.TYPE_NOT_FOUND, "type_not_found");

    private final int result;
    private final String resultDisplay;

    private DnsLookupResultType(int result, String resultDisplay) {
        this.result = result;
        this.resultDisplay = resultDisplay;
    }

    @Nullable
    public static DnsLookupResultType findByResult(int result) {
        for (DnsLookupResultType enumItem : EnumSet.allOf(DnsLookupResultType.class)) {
            if (enumItem.getResult() == result) {
                return enumItem;
            }
        }

        return UKNOWN;
    }

    @Override
    public String toString() {
        return this.resultDisplay;
    }

    public int getResult() {
        return this.result;
    }

    public String getResultDisplay() {
        return this.resultDisplay;
    }
}
