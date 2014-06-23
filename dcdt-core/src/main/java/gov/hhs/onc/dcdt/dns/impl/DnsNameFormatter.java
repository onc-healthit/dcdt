package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("formatterDnsName")
public class DnsNameFormatter extends AbstractToolFormatter<Name> {
    public DnsNameFormatter() {
        super(Name.class);
    }

    @Override
    protected Name parseInternal(String str, Locale locale) throws Exception {
        return ToolDnsNameUtils.fromString(str);
    }
}
