package gov.hhs.onc.dcdt.service.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.ToolService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@ServiceContextConfiguration({ "spring/spring-core.xml", "spring/spring-core*.xml", "spring/spring-service.xml", "spring/spring-service-embedded.xml",
    "spring/spring-service-standalone.xml" })
public abstract class AbstractToolService extends AbstractToolLifecycleBean implements ToolService {
    protected AbstractApplicationContext appContext;

    @Override
    public boolean canStart() {
        // noinspection ConstantConditions
        return (super.canStart() && ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class).isConfigured());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        this.appContext.registerShutdownHook();
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public int getOrder() {
        return this.getPhase();
    }
}
