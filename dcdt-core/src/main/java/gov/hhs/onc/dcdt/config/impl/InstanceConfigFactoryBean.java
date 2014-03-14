package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolFactoryBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class InstanceConfigFactoryBean extends AbstractToolFactoryBean<InstanceConfig> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;

    public InstanceConfigFactoryBean() {
        super(InstanceConfig.class);
    }

    @Override
    protected InstanceConfig createInstanceInternal() throws Exception {
        InstanceConfigService instanceConfigService = ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), InstanceConfigService.class);
        // noinspection ConstantConditions
        InstanceConfig instanceConfig = new InstanceConfigImpl(), instanceConfigExisting = instanceConfigService.getBean();

        if (instanceConfigExisting != null) {
            instanceConfig.setDomainName(instanceConfigExisting.getDomainName());

            instanceConfigService.loadBean(instanceConfig);
        }

        return instanceConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
