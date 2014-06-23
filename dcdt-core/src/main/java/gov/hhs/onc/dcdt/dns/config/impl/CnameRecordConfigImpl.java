package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.CnameRecordConfig;
import org.xbill.DNS.CNAMERecord;

public class CnameRecordConfigImpl extends AbstractTargetedDnsRecordConfig<CNAMERecord> implements CnameRecordConfig {
    public CnameRecordConfigImpl() {
        super(DnsRecordType.CNAME, CNAMERecord.class);
    }

    @Override
    public CNAMERecord toRecord() throws DnsException {
        return new CNAMERecord(this.name, this.recordType.getDclassType().getCode(), this.ttl, this.target);
    }
}
