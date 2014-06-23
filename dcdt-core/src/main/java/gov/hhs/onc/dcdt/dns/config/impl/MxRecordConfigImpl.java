package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.MxRecordConfig;
import org.xbill.DNS.MXRecord;

public class MxRecordConfigImpl extends AbstractPriorityTargetedDnsRecordConfig<MXRecord> implements MxRecordConfig {
    public MxRecordConfigImpl() {
        super(DnsRecordType.MX, MXRecord.class);
    }

    @Override
    public MXRecord toRecord() throws DnsException {
        return new MXRecord(this.name, this.recordType.getDclassType().getCode(), this.ttl, this.priority, this.target);
    }
}
