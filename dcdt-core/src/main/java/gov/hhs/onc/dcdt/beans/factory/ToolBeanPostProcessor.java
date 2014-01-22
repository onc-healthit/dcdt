package gov.hhs.onc.dcdt.beans.factory;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

public interface ToolBeanPostProcessor<T> extends ApplicationContextAware, BeanPostProcessor, PriorityOrdered {
    public boolean canPostProcessBeforeInitialization(T bean, String beanName);

    public boolean canPostProcessAfterInitialization(T bean, String beanName);
}
