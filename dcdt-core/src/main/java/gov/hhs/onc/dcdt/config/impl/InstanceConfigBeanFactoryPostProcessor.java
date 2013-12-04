package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanFactoryPostProcessor;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolBeanDefinitionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class InstanceConfigBeanFactoryPostProcessor extends AbstractToolBeanFactoryPostProcessor implements ApplicationContextAware {
    private final static String INSTANCE_CONFIGURED_PROFILE = "toolInstanceConfigured";

    private final static Logger LOGGER = LoggerFactory.getLogger(InstanceConfigBeanFactoryPostProcessor.class);

    private ConfigurableApplicationContext appContext;

    @Override
    protected void postProcessBeanFactoryInternal(ConfigurableListableBeanFactory beanFactory) throws ToolBeanException {
        ConfigurableEnvironment env = this.appContext.getEnvironment();

        if (!ArrayUtils.contains(env.getActiveProfiles(), INSTANCE_CONFIGURED_PROFILE)
            && ToolBeanDefinitionUtils.containsBeanDefinitionOfType(beanFactory, (BeanDefinitionRegistry)beanFactory, InstanceConfig.class)) {
            env.addActiveProfile(INSTANCE_CONFIGURED_PROFILE);

            LOGGER.debug("Instance configuration detected - adding active profile: " + INSTANCE_CONFIGURED_PROFILE);

            this.appContext.refresh();
        }

        super.postProcessBeanFactoryInternal(beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (ConfigurableApplicationContext) appContext;
    }
}
