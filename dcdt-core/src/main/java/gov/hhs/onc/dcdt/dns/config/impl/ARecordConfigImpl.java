package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.ARecordConfig;
import java.net.InetAddress;
import org.xbill.DNS.ARecord;

public class ARecordConfigImpl extends AbstractDnsRecordConfig<ARecord> implements ARecordConfig {
    private InetAddress addr;

    public ARecordConfigImpl() {
        super(DnsRecordType.A, ARecord.class);
    }

    @Override
    public ARecord toRecord() throws DnsException {
        return new ARecord(this.name, this.recordType.getDclassType().getCode(), this.ttl, this.addr);
    }

    @Override
    public InetAddress getAddress() {
        return this.addr;
    }

    @Override
    public void setAddress(InetAddress addr) {
        this.addr = addr;
    }
}
