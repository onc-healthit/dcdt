package gov.hhs.onc.dcdt.beans.factory.impl;


import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPostProcessor;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public abstract class AbstractToolBeanPostProcessor<T> extends AbstractToolBean implements ToolBeanPostProcessor<T> {
    protected Class<T> beanClass;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanPostProcessor.class);

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

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    protected T postProcessBeforeInitializationInternal(T bean, String beanName) throws ToolBeanException {
        LOGGER.debug(String.format("Post processed (class=%s) bean (name=%s, class=%s) before initialization.", ToolClassUtils.getName(this), beanName,
            ToolClassUtils.getName(bean)));

        return bean;
    }

    protected T postProcessAfterInitializationInternal(T bean, String beanName) throws ToolBeanException {
        LOGGER.debug(String.format("Post processed (class=%s) bean (name=%s, class=%s) after initialization.", ToolClassUtils.getName(this), beanName,
            ToolClassUtils.getName(bean)));

        return bean;
    }
}
