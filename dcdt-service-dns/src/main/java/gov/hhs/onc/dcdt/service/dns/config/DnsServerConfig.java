package gov.hhs.onc.dcdt.service.dns.config;

import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface DnsServerConfig extends ToolServerConfig {
    public List<InstanceDnsConfig> findAuthoritativeConfigs(Record questionRecord);

    public List<InstanceDnsConfig> findAuthoritativeConfigs(DnsRecordType questionRecordType, Name questionName);

    public boolean hasConfigs();

    @Nullable
    public List<InstanceDnsConfig> getConfigs();
}
