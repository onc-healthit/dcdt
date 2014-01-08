package gov.hhs.onc.dcdt.beans.factory;

import gov.hhs.onc.dcdt.beans.OverrideablePriorityOrdered;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface ToolBeanFactoryPostProcessor extends BeanFactoryPostProcessor, OverrideablePriorityOrdered {
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
