package gov.hhs.onc.dcdt.service.dns.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.AuthoritativeDnsConfigPredicate;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.Record;

public class DnsServerConfigImpl extends AbstractToolConnectionBean implements DnsServerConfig {
    @Autowired(required = false)
    private List<InstanceDnsConfig> dnsConfigs;

    @Override
    public List<InstanceDnsConfig> findAuthoritativeDnsConfigs(Record questionRecord) {
        return CollectionUtils.select(this.dnsConfigs, new AuthoritativeDnsConfigPredicate(questionRecord),
            new ArrayList<InstanceDnsConfig>(CollectionUtils.size(this.dnsConfigs)));
    }

    @Override
    public boolean hasDnsConfigs() {
        return !CollectionUtils.isEmpty(this.dnsConfigs);
    }

    @Nullable
    @Override
    public List<InstanceDnsConfig> getDnsConfigs() {
        return this.dnsConfigs;
    }
}
