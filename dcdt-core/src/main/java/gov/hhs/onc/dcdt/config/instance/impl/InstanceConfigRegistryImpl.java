package gov.hhs.onc.dcdt.config.instance.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
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
