package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Name;

public interface DnsNameLabelIdentifier extends DnsIdentifier {
    public final static String PROP_NAME_NAME_LBL = "nameLabel";

    public Name getNameLabel() throws DnsNameException;
}
