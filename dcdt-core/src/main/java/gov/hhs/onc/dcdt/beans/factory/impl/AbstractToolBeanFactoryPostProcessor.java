package gov.hhs.onc.dcdt.beans.factory.impl;


import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanFactoryPostProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class AbstractToolBeanFactoryPostProcessor implements ToolBeanFactoryPostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanFactoryPostProcessor.class);

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

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    protected void postProcessBeanFactoryInternal(ConfigurableListableBeanFactory beanFactory) throws ToolBeanException {
        LOGGER.debug(String.format("Post processed (class=%s) bean factory (class=%s).", ToolClassUtils.getName(this), ToolClassUtils.getName(beanFactory)));
    }
}
