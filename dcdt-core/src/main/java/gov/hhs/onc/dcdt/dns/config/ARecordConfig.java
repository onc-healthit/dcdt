package gov.hhs.onc.dcdt.dns.config;

import java.net.InetAddress;
import org.xbill.DNS.ARecord;

public interface ARecordConfig extends DnsRecordConfig<ARecord> {
    public InetAddress getAddress();

    public void setAddress(InetAddress addr);
}
