package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import org.xbill.DNS.Name;

public interface DnsNameLabelIdentifier extends ToolIdentifier {
    public Name getNameLabel() throws DnsNameException;
}
