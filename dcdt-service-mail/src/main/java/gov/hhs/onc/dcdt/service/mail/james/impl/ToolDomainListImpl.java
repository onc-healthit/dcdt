package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;
import gov.hhs.onc.dcdt.service.mail.james.ToolDomainList;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.james.domainlist.api.DomainListException;

public class ToolDomainListImpl extends AbstractToolBean implements ToolDomainList {
    private static class InstanceDomainConfigNameStringTransformer extends AbstractToolTransformer<InstanceDomainConfig, String> {
        public final static InstanceDomainConfigNameStringTransformer INSTANCE = new InstanceDomainConfigNameStringTransformer();

        @Nullable
        @Override
        protected String transformInternal(InstanceDomainConfig domainConfig) throws Exception {
            return ToolDomainListImpl.getDomainNameString(domainConfig);
        }
    }

    private InstanceDomainConfig defaultDomainConfig;
    private List<InstanceDomainConfig> domainConfigs = new ArrayList<>();

    @Override
    public String getDefaultDomain() throws DomainListException {
        String defaultDomainNameStr = getDomainNameString(this.defaultDomainConfig);

        if (defaultDomainNameStr == null) {
            throw new DomainListException(String.format("Default domain configuration (name=%s) in James domain list (class=%s) must have a domain name.",
                this.defaultDomainConfig.getName(), ToolClassUtils.getName(this)));
        }

        return getDomainNameString(this.defaultDomainConfig);
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
        return ToolCollectionUtils.toArray(
            CollectionUtils.select(
                CollectionUtils.collect(this.domainConfigs, InstanceDomainConfigNameStringTransformer.INSTANCE,
                    new LinkedHashSet<String>(this.domainConfigs.size())), PredicateUtils.notNullPredicate()), String.class);
    }

    @Nullable
    private static String getDomainNameString(InstanceDomainConfig domainConfig) throws DomainListException {
        return Objects.toString(domainConfig.getDomainName(), null);
    }

    @Override
    public InstanceDomainConfig getDefaultDomainConfig() {
        return this.defaultDomainConfig;
    }

    @Override
    public void setDefaultDomainConfig(InstanceDomainConfig defaultDomainConfig) {
        this.defaultDomainConfig = defaultDomainConfig;
    }

    @Override
    public List<InstanceDomainConfig> getDomainConfigs() {
        return this.domainConfigs;
    }

    @Override
    public void setDomainConfigs(List<InstanceDomainConfig> domainConfigs) {
        this.domainConfigs.clear();
        this.domainConfigs.addAll(domainConfigs);
    }
}
