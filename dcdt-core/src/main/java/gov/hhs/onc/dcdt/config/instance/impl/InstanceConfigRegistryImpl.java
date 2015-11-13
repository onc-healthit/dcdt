package gov.hhs.onc.dcdt.config.instance.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialRegistry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component("instanceConfigRegistryImpl")
public class InstanceConfigRegistryImpl extends AbstractToolBeanRegistry<InstanceConfig, InstanceConfigDao, InstanceConfigService> implements
    InstanceConfigRegistry {
    public InstanceConfigRegistryImpl() {
        super(InstanceConfig.class, InstanceConfigService.class);
    }

    @Override
    protected void postRegisterBeans(List<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();

        this.getDiscoveryTestcaseCredentialRegistry().registerBeans(this.extractDiscoveryTestcaseCredentials());

        this.appContext.refresh();
    }

    @Override
    protected void preRemoveBeans(List<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseCredentialRegistry().removeBeans(this.extractDiscoveryTestcaseCredentials());
    }

    @Override
    protected void postRemoveBeans(List<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();
    }

    protected List<DiscoveryTestcaseCredential> extractDiscoveryTestcaseCredentials() {
        // noinspection ConstantConditions
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class).stream()
            .flatMap(bean -> (bean.hasCredentials() ? bean.getCredentials().stream() : Stream.<DiscoveryTestcaseCredential>empty()))
            .collect(Collectors.toList());
    }

    protected DiscoveryTestcaseCredentialRegistry getDiscoveryTestcaseCredentialRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext, DiscoveryTestcaseCredentialRegistry.class);
    }
}
