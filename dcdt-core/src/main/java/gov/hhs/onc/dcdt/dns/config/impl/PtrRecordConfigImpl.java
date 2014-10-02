package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.PtrRecordConfig;
import org.xbill.DNS.PTRRecord;

public class PtrRecordConfigImpl extends AbstractTargetedDnsRecordConfig<PTRRecord> implements PtrRecordConfig {
    public PtrRecordConfigImpl() {
        super(DnsRecordType.PTR, PTRRecord.class);
    }

    @Override
    public PTRRecord toRecord() throws DnsException {
        return new PTRRecord(this.name, this.recordType.getDclassType().getCode(), this.ttl, this.target);
    }
}
