package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanFactoryPostProcessor;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolBeanFactoryPostProcessor extends AbstractToolOrderedBean implements ToolBeanFactoryPostProcessor {
    protected AbstractApplicationContext appContext;
    protected boolean postProcBeanFactory;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanFactoryPostProcessor.class);

    protected AbstractToolBeanFactoryPostProcessor(boolean postProcBeanFactory) {
        this.postProcBeanFactory = postProcBeanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (this.canPostProcessBeanFactory(beanFactory)) {
            this.postProcessBeanFactoryInternal(beanFactory);

            LOGGER
                .debug(String.format("Post processed (class=%s) bean factory (class=%s).", ToolClassUtils.getName(this), ToolClassUtils.getName(beanFactory)));
        }
    }

    @Override
    public boolean canPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        return this.postProcBeanFactory;
    }

    protected void postProcessBeanFactoryInternal(ConfigurableListableBeanFactory beanFactory) throws ToolBeanException {
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
