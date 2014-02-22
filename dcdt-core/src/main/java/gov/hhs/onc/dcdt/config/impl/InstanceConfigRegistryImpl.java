package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import org.springframework.stereotype.Component;

@Component("instanceConfigRegistryImpl")
public class InstanceConfigRegistryImpl extends AbstractToolBeanRegistry<InstanceConfig, InstanceConfigDao, InstanceConfigService> implements
    InstanceConfigRegistry {
    public InstanceConfigRegistryImpl() {
        super(InstanceConfig.class, InstanceConfigService.class);
    }

    @Override
    protected void preRemoveBeans(Iterable<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.getDiscoveryTestcaseRegistry().removeAllBeans();
    }

    @Override
    protected void postRegisterBeans(Iterable<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();

        this.getDiscoveryTestcaseRegistry().registerAllBeans();
    }

    @Override
    protected void postRemoveBeans(Iterable<InstanceConfig> beans) throws ToolBeanRegistryException {
        this.appContext.refresh();
    }

    protected DiscoveryTestcaseRegistry getDiscoveryTestcaseRegistry() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseRegistry.class);
    }
}
