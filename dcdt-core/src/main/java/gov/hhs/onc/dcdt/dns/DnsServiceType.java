package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import org.xbill.DNS.Name;

public enum DnsServiceType implements DnsNameLabel {
    LDAP("_ldap");

    private final String serviceType;

    private DnsServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public Name getDnsNameLabel() throws DnsNameException {
        return ToolDnsNameUtils.fromLabelStrings(this.serviceType);
    }

    @Override
    public String getDnsNameLabelString() {
        return this.serviceType;
    }

    @Override
    public String toString() {
        return this.serviceType;
    }

    public String getServiceType() {
        return this.serviceType;
    }
}
