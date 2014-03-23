package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import javax.annotation.Nullable;

@ConfigurationNode(name = "dnsservice")
public interface DnsServiceConfigBean extends JamesConfigBean {
    public boolean hasAutoDiscover();

    @ConfigurationNode(name = "autodiscover")
    @Nullable
    public Boolean isAutoDiscover();

    public void setAutoDiscover(@Nullable Boolean autoDiscover);

    public boolean hasMaxCacheSize();

    @ConfigurationNode(name = "maxcachesize")
    @Nullable
    public Integer getMaxCacheSize();

    public void setMaxCacheSize(@Nullable Integer maxCacheSize);

    public boolean hasServers();

    @ConfigurationNode
    @Nullable
    public DnsServiceServersConfigBean getServers();

    public void setServers(@Nullable DnsServiceServersConfigBean servers);
}
