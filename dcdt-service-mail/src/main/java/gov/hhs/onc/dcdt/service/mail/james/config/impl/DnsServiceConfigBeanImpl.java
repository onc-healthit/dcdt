package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import javax.annotation.Nullable;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Resolver;

public class DnsServiceConfigBeanImpl extends AbstractJamesConfigBean implements DnsServiceConfigBean {
    private Boolean autoDiscover;
    private Cache cache;
    private Resolver extResolver;
    private Resolver localResolver;

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
    public boolean hasExternalResolver() {
        return (this.extResolver != null);
    }

    @Nullable
    @Override
    public Resolver getExternalResolver() {
        return this.extResolver;
    }

    @Override
    public void setExternalResolver(@Nullable Resolver extResolver) {
        this.extResolver = extResolver;
    }

    @Override
    public boolean hasLocalResolver() {
        return (this.localResolver != null);
    }

    @Nullable
    @Override
    public Resolver getLocalResolver() {
        return this.localResolver;
    }

    @Override
    public void setLocalResolver(@Nullable Resolver localResolver) {
        this.localResolver = localResolver;
    }
}
