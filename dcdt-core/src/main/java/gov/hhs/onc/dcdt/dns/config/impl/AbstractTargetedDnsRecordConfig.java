package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.TargetedDnsRecordConfig;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public abstract class AbstractTargetedDnsRecordConfig<T extends Record> extends AbstractDnsRecordConfig<T> implements TargetedDnsRecordConfig<T> {
    protected Name target;

    protected AbstractTargetedDnsRecordConfig(DnsRecordType recordType, Class<T> recordClass) {
        super(recordType, recordClass);
    }

    @Override
    public Name getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(Name target) {
        this.target = target;
    }
}
