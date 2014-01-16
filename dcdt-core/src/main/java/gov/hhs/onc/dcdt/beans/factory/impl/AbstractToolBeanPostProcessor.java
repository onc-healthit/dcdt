package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPostProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanPostProcessor<T> implements ToolBeanPostProcessor<T> {
    protected ApplicationContext appContext;
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
        return false;
    }

    @Override
    public boolean canPostProcessAfterInitialization(T bean, String beanName) {
        return false;
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

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }
}
