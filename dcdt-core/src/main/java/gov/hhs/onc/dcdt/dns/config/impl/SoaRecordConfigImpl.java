package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.SoaRecordConfig;
import org.xbill.DNS.Name;
import org.xbill.DNS.SOARecord;

public class SoaRecordConfigImpl extends AbstractDnsRecordConfig<SOARecord> implements SoaRecordConfig {
    private Name admin;
    private long expire;
    private Name host;
    private long min;
    private long refresh;
    private long retry;
    private long serial;

    public SoaRecordConfigImpl() {
        super(DnsRecordType.SOA, SOARecord.class);
    }

    @Override
    public SOARecord toRecord() throws DnsException {
        return new SOARecord(this.name, this.recordType.getDclass(), this.ttl, this.host, this.admin, this.serial, this.refresh, this.retry, this.expire,
            this.min);
    }

    @Override
    public Name getAdmin() {
        return this.admin;
    }

    @Override
    public void setAdmin(Name admin) {
        this.admin = admin;
    }

    @Override
    public long getExpire() {
        return this.expire;
    }

    @Override
    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public Name getHost() {
        return this.host;
    }

    @Override
    public void setHost(Name host) {
        this.host = host;
    }

    @Override
    public long getMinimum() {
        return this.min;
    }

    @Override
    public void setMinimum(long min) {
        this.min = min;
    }

    @Override
    public long getRefresh() {
        return this.refresh;
    }

    @Override
    public void setRefresh(long refresh) {
        this.refresh = refresh;
    }

    @Override
    public long getRetry() {
        return this.retry;
    }

    @Override
    public void setRetry(long retry) {
        this.retry = retry;
    }

    @Override
    public long getSerial() {
        return this.serial;
    }

    @Override
    public void setSerial(long serial) {
        this.serial = serial;
    }
}
