package gov.hhs.onc.dcdt.beans.factory;


import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

public interface ToolBeanPostProcessor<T> extends BeanPostProcessor, PriorityOrdered, ToolBean {
    public boolean canPostProcessBeforeInitialization(T bean, String beanName);

    public boolean canPostProcessAfterInitialization(T bean, String beanName);
}
