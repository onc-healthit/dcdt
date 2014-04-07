package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.PriorityTargetedDnsRecordConfig;
import org.xbill.DNS.Record;

public abstract class AbstractPriorityTargetedDnsRecordConfig<T extends Record> extends AbstractTargetedDnsRecordConfig<T> implements
    PriorityTargetedDnsRecordConfig<T> {
    protected int priority;

    protected AbstractPriorityTargetedDnsRecordConfig(DnsRecordType recordType, Class<T> recordClass) {
        super(recordType, recordClass);
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
