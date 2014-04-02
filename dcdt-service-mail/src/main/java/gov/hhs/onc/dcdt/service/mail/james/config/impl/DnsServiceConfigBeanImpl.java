package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import javax.annotation.Nullable;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Resolver;

public class DnsServiceConfigBeanImpl extends AbstractJamesConfigBean implements DnsServiceConfigBean {
    private Boolean autoDiscover;
    private Cache cache;
    private Resolver resolver;

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
    public boolean hasCache() {
        return (this.cache != null);
    }

    @Nullable
    @Override
    public Cache getCache() {
        return this.cache;
    }

    @Override
    public void setCache(@Nullable Cache cache) {
        this.cache = cache;
    }

    @Override
    public boolean hasResolver() {
        return (this.resolver != null);
    }

    @Nullable
    @Override
    public Resolver getResolver() {
        return this.resolver;
    }

    @Override
    public void setResolver(@Nullable Resolver resolver) {
        this.resolver = resolver;
    }
}
