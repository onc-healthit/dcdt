package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;
import gov.hhs.onc.dcdt.service.mail.james.ToolDomainList;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.james.domainlist.api.DomainListException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.xbill.DNS.Name;

public class ToolDomainListImpl extends AbstractToolBean implements ToolDomainList {
    private AbstractApplicationContext appContext;
    private Name defaultDomainName;
    private Name defaultDomainNameFallback;
    private List<InstanceDomainConfig> domainConfigs;

    @Override
    public String getDefaultDomain() throws DomainListException {
        return getDomainNameString((this.hasDefaultDomainName() ? this.defaultDomainName : this.defaultDomainNameFallback));
    }

    @Override
    public void removeDomain(String domainNameStr) throws DomainListException {
        throw new DomainListException(String.format("Unable to remove domain (name=%s) from James domain list (class=%s).", domainNameStr,
            ToolClassUtils.getName(this)));
    }

    @Override
    public void addDomain(String domainNameStr) throws DomainListException {
        throw new DomainListException(String.format("Unable to add domain (name=%s) to James domain list (class=%s).", domainNameStr,
            ToolClassUtils.getName(this)));
    }

    @Override
    public boolean containsDomain(String domainNameStr) throws DomainListException {
        return ArrayUtils.contains(this.getDomains(), domainNameStr);
    }

    @Override
    public String[] getDomains() throws DomainListException {
        return ToolCollectionUtils.toArray(ToolStreamUtils.filter(ToolStreamUtils.transform(this.domainConfigs, ToolTransformer.wrap(
            ToolDomainListImpl::getDomainNameString), LinkedHashSet::new), Objects::nonNull), String.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.domainConfigs = ToolBeanFactoryUtils.getBeansOfType(this.appContext, InstanceDomainConfig.class);
    }

    @Nullable
    private static String getDomainNameString(InstanceDomainConfig domainConfig) throws DomainListException {
        return getDomainNameString(domainConfig.getDomainName());
    }

    @Nullable
    private static String getDomainNameString(Name domainName) throws DomainListException {
        return Objects.toString(domainName, null);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public boolean hasDefaultDomainName() {
        return (this.defaultDomainName != null);
    }

    @Nullable
    @Override
    public Name getDefaultDomainName() {
        return this.defaultDomainName;
    }

    @Override
    public void setDefaultDomainName(@Nullable Name defaultDomainName) {
        this.defaultDomainName = defaultDomainName;
    }

    @Override
    public Name getDefaultDomainNameFallback() {
        return this.defaultDomainNameFallback;
    }

    @Override
    public void setDefaultDomainNameFallback(Name defaultDomainNameFallback) {
        this.defaultDomainNameFallback = defaultDomainNameFallback;
    }

    @Override
    public boolean hasDomainConfigs() {
        return !CollectionUtils.isEmpty(this.domainConfigs);
    }

    @Override
    public List<InstanceDomainConfig> getDomainConfigs() {
        return this.domainConfigs;
    }
}
