package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Name;

public interface DnsNameLabel {
    public Name getDnsNameLabel() throws DnsNameException;

    public String getDnsNameLabelString();
}
