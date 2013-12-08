package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanDefinitionRegistryPostProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class AbstractToolBeanDefinitionRegistryPostProcessor extends AbstractToolBeanFactoryPostProcessor implements ToolBeanDefinitionRegistryPostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanDefinitionRegistryPostProcessor.class);

    private BeanDefinitionRegistry beanDefReg;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) throws BeansException {
        if (this.canPostProcessBeanDefinitionRegistry(beanDefReg)) {
            this.postProcessBeanDefinitionRegistryInternal(beanDefReg);
        }

        this.beanDefReg = beanDefReg;
    }

    @Override
    public boolean canPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        return true;
    }

    protected void postProcessBeanDefinitionRegistryInternal(BeanDefinitionRegistry beanDefReg) throws ToolBeanException {
        LOGGER.debug(String.format("Post processed (class=%s) bean definition registry (class=%s).", ToolClassUtils.getName(this),
            ToolClassUtils.getName(beanDefReg)));
    }
}
