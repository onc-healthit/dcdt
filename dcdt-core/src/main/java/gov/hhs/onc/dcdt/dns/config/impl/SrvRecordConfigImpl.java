package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import org.xbill.DNS.SRVRecord;

public class SrvRecordConfigImpl extends AbstractPriorityTargetedDnsRecordConfig<SRVRecord> implements SrvRecordConfig {
    private int port;
    private DnsServiceProtocol serviceProtocol;
    private DnsServiceType serviceType;
    private int weight;

    public SrvRecordConfigImpl() {
        super(DnsRecordType.SRV, SRVRecord.class);
    }

    @Override
    public SRVRecord toRecord() throws DnsException {
        return new SRVRecord(ToolDnsNameUtils.fromLabels(this.serviceType.getDnsNameLabel(), this.serviceProtocol.getDnsNameLabel(), this.name),
            this.recordType.getDclass(), this.ttl, this.priority, this.weight, this.port, this.target);
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public DnsServiceProtocol getServiceProtocol() {
        return this.serviceProtocol;
    }

    @Override
    public void setServiceProtocol(DnsServiceProtocol serviceProtocol) {
        this.serviceProtocol = serviceProtocol;
    }

    @Override
    public DnsServiceType getServiceType() {
        return this.serviceType;
    }

    @Override
    public void setServiceType(DnsServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
