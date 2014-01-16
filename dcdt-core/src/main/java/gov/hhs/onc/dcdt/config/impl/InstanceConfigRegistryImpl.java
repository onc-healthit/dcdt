package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigDao;
import gov.hhs.onc.dcdt.config.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.impl.AbstractToolBeanRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseRegistry;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("instanceConfigRegistryImpl")
@Scope("singleton")
public class InstanceConfigRegistryImpl extends AbstractToolBeanRegistry<InstanceConfig, InstanceConfigDao, InstanceConfigService> implements
    InstanceConfigRegistry {
    public InstanceConfigRegistryImpl() {
        super(InstanceConfig.class, InstanceConfigService.class);
    }

    @Override
    public void registerBeans(Iterable<InstanceConfig> beans) throws ToolBeanRegistryException {
        super.registerBeans(beans);

        ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseRegistry.class).registerAllBeans();
    }

    @Override
    public void removeBeans(Iterable<InstanceConfig> beans) throws ToolBeanRegistryException {
        ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), DiscoveryTestcaseRegistry.class).removeAllBeans();

        super.removeBeans(beans);
    }
}
