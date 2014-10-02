package gov.hhs.onc.dcdt.service.dns.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Record;

public interface DnsServerConfig extends ToolConnectionBean {
    public List<InstanceDnsConfig> findAuthoritativeDnsConfigs(Record questionRecord);

    public boolean hasDnsConfigs();

    @Nullable
    public List<InstanceDnsConfig> getDnsConfigs();
}
