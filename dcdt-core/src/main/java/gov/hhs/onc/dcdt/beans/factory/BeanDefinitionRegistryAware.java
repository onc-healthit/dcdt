package gov.hhs.onc.dcdt.beans.factory;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public interface BeanDefinitionRegistryAware {
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg);
}
