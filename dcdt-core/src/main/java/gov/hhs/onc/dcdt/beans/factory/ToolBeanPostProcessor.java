package gov.hhs.onc.dcdt.beans.factory;

import gov.hhs.onc.dcdt.beans.ToolOrderedBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContextAware;

public interface ToolBeanPostProcessor<T> extends ApplicationContextAware, BeanPostProcessor, ToolOrderedBean {
    public boolean canPostProcessBeforeInitialization(T bean, String beanName);

    public boolean canPostProcessAfterInitialization(T bean, String beanName);
}
