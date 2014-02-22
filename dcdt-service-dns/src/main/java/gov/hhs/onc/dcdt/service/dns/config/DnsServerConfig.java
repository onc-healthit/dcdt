package gov.hhs.onc.dcdt.service.dns.config;

import gov.hhs.onc.dcdt.beans.ToolBoundBean;
import gov.hhs.onc.dcdt.config.InstanceDnsConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Record;

public interface DnsServerConfig extends ToolBoundBean {
    @Nullable
    public InstanceDnsConfig findAuthoritativeDnsConfig(Record questionRecord);

    public boolean hasDnsConfigs();

    @Nullable
    public List<InstanceDnsConfig> getDnsConfigs();
}
