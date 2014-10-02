package gov.hhs.onc.dcdt.dns.config;

import java.util.List;
import org.xbill.DNS.TXTRecord;

public interface TxtRecordConfig extends DnsRecordConfig<TXTRecord> {
    public List<String> getStrings();

    public void setStrings(List<String> strs);
}
