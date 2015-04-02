package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;

public interface DnsCodeIdentifier {
    @Nonnegative
    public int getCode();
}
