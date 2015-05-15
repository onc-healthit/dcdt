package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;

public interface DnsCodeIdentifier {
    public final static String PROP_NAME_CODE = "code";

    @Nonnegative
    public int getCode();
}
