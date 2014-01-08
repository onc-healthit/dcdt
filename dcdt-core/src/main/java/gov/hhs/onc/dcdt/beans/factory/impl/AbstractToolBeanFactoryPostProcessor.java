package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanFactoryPostProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;

public abstract class AbstractToolBeanFactoryPostProcessor implements ToolBeanFactoryPostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanFactoryPostProcessor.class);

    protected AbstractApplicationContext appContext;
    protected int order = Ordered.LOWEST_PRECEDENCE;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (this.canPostProcessBeanFactory(beanFactory)) {
            this.postProcessBeanFactoryInternal(beanFactory);
        }
    }

    @Override
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        return true;
    }

    protected void postProcessBeanFactoryInternal(ConfigurableListableBeanFactory beanFactory) throws ToolBeanException {
        LOGGER.debug(String.format("Post processed (class=%s) bean factory (class=%s).", ToolClassUtils.getName(this), ToolClassUtils.getName(beanFactory)));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public int getOrder() {
        return ToolOrderUtils.getOrder(this, this.order);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }
}
