package gov.hhs.onc.dcdt.beans.factory;

import gov.hhs.onc.dcdt.beans.OverrideablePriorityOrdered;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextAware;

public interface ToolBeanFactoryPostProcessor extends ApplicationContextAware, BeanFactoryPostProcessor, OverrideablePriorityOrdered {
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
