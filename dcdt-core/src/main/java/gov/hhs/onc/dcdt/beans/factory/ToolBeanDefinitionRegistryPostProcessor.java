package gov.hhs.onc.dcdt.beans.factory;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public interface ToolBeanDefinitionRegistryPostProcessor extends BeanDefinitionRegistryPostProcessor, ToolBeanFactoryPostProcessor {
    public boolean canPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg);
}
