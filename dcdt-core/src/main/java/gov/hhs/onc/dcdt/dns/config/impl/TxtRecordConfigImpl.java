package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.TxtRecordConfig;
import java.util.List;
import org.xbill.DNS.TXTRecord;

public class TxtRecordConfigImpl extends AbstractDnsRecordConfig<TXTRecord> implements TxtRecordConfig {
    private List<String> strs;

    public TxtRecordConfigImpl() {
        super(DnsRecordType.TXT, TXTRecord.class);
    }

    @Override
    public TXTRecord toRecord() throws DnsException {
        return new TXTRecord(this.name, this.recordType.getDclassType().getCode(), this.ttl, this.strs);
    }

    @Override
    public List<String> getStrings() {
        return this.strs;
    }

    @Override
    public void setStrings(List<String> strs) {
        this.strs = strs;
    }
}
