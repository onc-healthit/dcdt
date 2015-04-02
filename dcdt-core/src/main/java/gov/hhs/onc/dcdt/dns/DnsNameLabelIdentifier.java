package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Name;

public interface DnsNameLabelIdentifier extends DnsIdentifier {
    public Name getNameLabel() throws DnsNameException;
}
