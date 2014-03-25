package gov.hhs.onc.dcdt.dns.config;

import org.xbill.DNS.Record;

public interface PriorityTargetedDnsRecordConfig<T extends Record> extends TargetedDnsRecordConfig<T> {
    public int getPriority();

    public void setPriority(int priority);
}
