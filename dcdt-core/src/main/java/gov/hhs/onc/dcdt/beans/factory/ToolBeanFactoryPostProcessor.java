package gov.hhs.onc.dcdt.beans.factory;


import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

public interface ToolBeanFactoryPostProcessor extends BeanFactoryPostProcessor, PriorityOrdered {
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
