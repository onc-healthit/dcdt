package gov.hhs.onc.dcdt.service.dns.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class DnsServerConfigImpl extends AbstractToolConnectionBean implements DnsServerConfig {
    @Autowired(required = false)
    private List<InstanceDnsConfig> configs;

    @Override
    public List<InstanceDnsConfig> findAuthoritativeConfigs(Record questionRecord) {
        // noinspection ConstantConditions
        return this.findAuthoritativeConfigs(ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType()), questionRecord.getName());
    }

    @Override
    public List<InstanceDnsConfig> findAuthoritativeConfigs(DnsRecordType questionRecordType, Name questionName) {
        return (List<InstanceDnsConfig>) ToolStreamUtils.filter(this.configs, dnsConfig -> dnsConfig.isAuthoritative(questionRecordType, questionName));
    }

    @Override
    public boolean hasConfigs() {
        return !CollectionUtils.isEmpty(this.configs);
    }

    @Nullable
    @Override
    public List<InstanceDnsConfig> getConfigs() {
        return this.configs;
    }
}
