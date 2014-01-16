package gov.hhs.onc.dcdt.dns.config;

import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import org.xbill.DNS.SRVRecord;

public interface SrvRecordConfig extends PriorityTargetedDnsRecordConfig<SRVRecord> {
    public int getPort();

    public void setPort(int port);

    public DnsServiceProtocol getServiceProtocol();

    public void setServiceProtocol(DnsServiceProtocol serviceProtocol);

    public DnsServiceType getServiceType();

    public void setServiceType(DnsServiceType serviceType);

    public int getWeight();

    public void setWeight(int weight);
}
