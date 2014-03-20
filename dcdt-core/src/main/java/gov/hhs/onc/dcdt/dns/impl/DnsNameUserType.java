package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("dnsNameUserType")
public class DnsNameUserType extends AbstractStringUserType<Name> {
    public DnsNameUserType() {
        super(Name.class);
    }
}
