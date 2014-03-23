package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPostProcessor;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanPostProcessor<T> extends AbstractToolOrderedBean implements ToolBeanPostProcessor<T> {
    protected ApplicationContext appContext;
    protected Class<T> beanClass;
    protected boolean postProcBeforeInit;
    protected boolean postProcAfterInit;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanPostProcessor.class);

    protected AbstractToolBeanPostProcessor(Class<T> beanClass, boolean postProcBeforeInit, boolean postProcAfterInit) {
        this.beanClass = beanClass;
        this.postProcBeforeInit = postProcBeforeInit;
        this.postProcAfterInit = postProcAfterInit;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.beanClass.isAssignableFrom(bean.getClass())) {
            return bean;
        }

        T beanCast = this.beanClass.cast(bean);

        if (this.canPostProcessAfterInitialization(beanCast, beanName)) {
            try {
                bean = this.postProcessAfterInitializationInternal(beanCast, beanName);

                LOGGER.debug(String.format("Post processed (class=%s) bean (name=%s, class=%s) after initialization.", ToolClassUtils.getName(this), beanName,
                    ToolClassUtils.getName(bean)));
            } catch (Exception e) {
                throw new ToolBeanException(String.format("Unable to post process (class=%s) bean (name=%s, class=%s) after initialization.",
                    ToolClassUtils.getName(this), beanName, ToolClassUtils.getName(bean)), e);
            }
        }

        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!this.beanClass.isAssignableFrom(bean.getClass())) {
            return bean;
        }

        T beanCast = this.beanClass.cast(bean);

        if (this.canPostProcessBeforeInitialization(beanCast, beanName)) {
            try {
                bean = this.postProcessBeforeInitializationInternal(beanCast, beanName);

                LOGGER.debug(String.format("Post processed (class=%s) bean (name=%s, class=%s) before initialization.", ToolClassUtils.getName(this), beanName,
                    ToolClassUtils.getName(bean)));
            } catch (Exception e) {
                throw new ToolBeanException(String.format("Unable to post process (class=%s) bean (name=%s, class=%s) before initialization.",
                    ToolClassUtils.getName(this), beanName, ToolClassUtils.getName(bean)), e);
            }
        }

        return bean;
    }

    @Override
    public boolean canPostProcessAfterInitialization(T bean, String beanName) {
        return this.postProcAfterInit;
    }

    @Override
    public boolean canPostProcessBeforeInitialization(T bean, String beanName) {
        return this.postProcBeforeInit;
    }

    protected T postProcessAfterInitializationInternal(T bean, String beanName) throws Exception {
        return bean;
    }

    protected T postProcessBeforeInitializationInternal(T bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }
}
