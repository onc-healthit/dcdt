package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import org.xbill.DNS.Name;

public enum DnsServiceType implements DnsNameLabelIdentifier {
    LDAP("_ldap");

    private final String id;

    private DnsServiceType(String id) {
        this.id = id;
    }

    @Override
    public Name getNameLabel() throws DnsNameException {
        return ToolDnsNameUtils.fromLabelStrings(this.id);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
