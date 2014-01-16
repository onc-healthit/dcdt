package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.NsRecordConfig;
import org.xbill.DNS.NSRecord;

public class NsRecordConfigImpl extends AbstractTargetedDnsRecordConfig<NSRecord> implements NsRecordConfig {
    public NsRecordConfigImpl() {
        super(DnsRecordType.NS, NSRecord.class);
    }

    @Override
    public NSRecord toRecord() throws DnsException {
        return new NSRecord(this.name, this.recordType.getDclass(), this.ttl, this.target);
    }
}
