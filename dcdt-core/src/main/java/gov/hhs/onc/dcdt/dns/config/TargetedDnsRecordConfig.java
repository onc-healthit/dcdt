package gov.hhs.onc.dcdt.dns.config;

import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface TargetedDnsRecordConfig<T extends Record> extends DnsRecordConfig<T> {
    public Name getTarget();

    public void setTarget(Name target);
}
