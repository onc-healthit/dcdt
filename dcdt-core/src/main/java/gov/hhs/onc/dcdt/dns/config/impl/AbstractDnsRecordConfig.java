package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public abstract class AbstractDnsRecordConfig<T extends Record> extends AbstractToolBean implements DnsRecordConfig<T> {
    protected DnsRecordType recordType;
    protected Class<T> recordClass;
    protected Name name;
    protected long ttl;

    protected AbstractDnsRecordConfig(DnsRecordType recordType, Class<T> recordClass) {
        this.recordType = recordType;
        this.recordClass = recordClass;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public DnsRecordType getRecordType() {
        return this.recordType;
    }
}
