package gov.hhs.onc.dcdt.beans.factory.impl;


import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPostProcessor;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import org.springframework.beans.BeansException;

public abstract class AbstractToolBeanPostProcessor<T> extends AbstractToolBean implements ToolBeanPostProcessor<T> {
    protected Class<T> beanClass;

    protected AbstractToolBeanPostProcessor(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!this.beanClass.isAssignableFrom(bean.getClass())) {
            return bean;
        }

        T beanCast = this.beanClass.cast(bean);

        return this.canPostProcessBeforeInitialization(beanCast, beanName) ? this.postProcessBeforeInitializationInternal(beanCast, beanName) : bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.beanClass.isAssignableFrom(bean.getClass())) {
            return bean;
        }

        T beanCast = this.beanClass.cast(bean);

        return this.canPostProcessAfterInitialization(beanCast, beanName) ? this.postProcessAfterInitializationInternal(beanCast, beanName) : bean;
    }

    @Override
    public boolean canPostProcessBeforeInitialization(T bean, String beanName) {
        return true;
    }

    @Override
    public boolean canPostProcessAfterInitialization(T bean, String beanName) {
        return true;
    }

    protected T postProcessBeforeInitializationInternal(T bean, String beanName) throws ToolBeanException {
        return bean;
    }

    protected T postProcessAfterInitializationInternal(T bean, String beanName) throws ToolBeanException {
        return bean;
    }
}
