package gov.hhs.onc.dcdt.beans.factory;

import gov.hhs.onc.dcdt.beans.ToolOrderedBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextAware;

public interface ToolBeanFactoryPostProcessor extends ApplicationContextAware, BeanFactoryPostProcessor, ToolOrderedBean {
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
