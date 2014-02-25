package gov.hhs.onc.dcdt.service.dns.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBoundBean;
import gov.hhs.onc.dcdt.config.InstanceDnsConfig;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.Record;

public class DnsServerConfigImpl extends AbstractToolBoundBean implements DnsServerConfig {
    @Autowired(required = false)
    private List<InstanceDnsConfig> dnsConfigs;

    @Nullable
    @Override
    public InstanceDnsConfig findAuthoritativeDnsConfig(Record questionRecord) {
        InstanceDnsConfig dnsConfigAuthoritative = null;

        if (this.hasDnsConfigs()) {
            for (InstanceDnsConfig dnsConfig : this.dnsConfigs) {
                // noinspection ConstantConditions
                if (dnsConfig.isAuthoritative(questionRecord)
                    && ((dnsConfigAuthoritative == null) || dnsConfig.getDomainName().subdomain(dnsConfigAuthoritative.getDomainName()))) {
                    dnsConfigAuthoritative = dnsConfig;
                }
            }
        }

        return dnsConfigAuthoritative;
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
