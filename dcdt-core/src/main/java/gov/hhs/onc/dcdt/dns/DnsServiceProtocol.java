package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import org.xbill.DNS.Name;

public enum DnsServiceProtocol implements DnsNameLabel {
    TCP("_tcp"), UDP("_udp");

    private final String serviceProtocol;

    private DnsServiceProtocol(String serviceProtocol) {
        this.serviceProtocol = serviceProtocol;
    }

    @Override
    public Name getDnsNameLabel() throws DnsNameException {
        return ToolDnsNameUtils.fromLabelStrings(this.serviceProtocol);
    }

    @Override
    public String getDnsNameLabelString() {
        return this.serviceProtocol;
    }

    @Override
    public String toString() {
        return this.serviceProtocol;
    }

    public String getServiceProtocol() {
        return this.serviceProtocol;
    }
}
