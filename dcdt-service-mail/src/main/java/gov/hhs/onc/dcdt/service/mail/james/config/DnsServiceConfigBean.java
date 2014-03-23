package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import javax.annotation.Nullable;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Resolver;

@ConfigurationNode(name = "dnsservice")
public interface DnsServiceConfigBean extends JamesConfigBean {
    public boolean hasAutoDiscover();

    @ConfigurationNode(name = "autodiscover")
    @Nullable
    public Boolean isAutoDiscover();

    public void setAutoDiscover(@Nullable Boolean autoDiscover);

    public boolean hasCache();

    @Nullable
    public Cache getCache();

    public void setCache(@Nullable Cache cache);

    public boolean hasResolver();

    @Nullable
    public Resolver getResolver();

    public void setResolver(@Nullable Resolver resolver);
}
