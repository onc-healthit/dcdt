package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceServersConfigBean;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nullable;

public class DnsServiceConfigBeanImpl extends AbstractJamesConfigBean implements DnsServiceConfigBean {
    private Boolean autoDiscover;
    private Integer maxCacheSize;
    private DnsServiceServersConfigBean servers;

    @Override
    public boolean hasAutoDiscover() {
        return (this.autoDiscover != null);
    }

    @Nullable
    @Override
    public Boolean isAutoDiscover() {
        return this.autoDiscover;
    }

    @Override
    public void setAutoDiscover(@Nullable Boolean autoDiscover) {
        this.autoDiscover = autoDiscover;
    }

    @Override
    public boolean hasMaxCacheSize() {
        return !ToolNumberUtils.isNegative(this.maxCacheSize);
    }

    @Nullable
    @Override
    public Integer getMaxCacheSize() {
        return this.maxCacheSize;
    }

    @Override
    public void setMaxCacheSize(@Nullable Integer maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public boolean hasServers() {
        return (this.servers != null);
    }

    @Nullable
    @Override
    public DnsServiceServersConfigBean getServers() {
        return this.servers;
    }

    @Override
    public void setServers(@Nullable DnsServiceServersConfigBean servers) {
        this.servers = servers;
    }
}
