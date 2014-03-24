package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolFactoryBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DiscoveryTestcaseMailMappingFactoryBean extends AbstractToolFactoryBean<DiscoveryTestcaseMailMapping> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;

    private DiscoveryTestcaseMailMappingFactoryBean() {
        super(DiscoveryTestcaseMailMapping.class);
    }

    @Override
    protected DiscoveryTestcaseMailMapping createInstanceInternal() throws Exception {
        DiscoveryTestcaseMailMapping discoveryTestcaseMailMapping = new DiscoveryTestcaseMailMappingImpl();
        // noinspection ConstantConditions
        ToolBeanFactoryUtils.getBeanOfType(this.appContext, DiscoveryTestcaseMailMappingService.class).loadBean(discoveryTestcaseMailMapping);

        return discoveryTestcaseMailMapping;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
