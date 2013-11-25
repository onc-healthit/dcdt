package gov.hhs.onc.dcdt.web.controller.impl;


import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanFactoryPostProcessor;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.Ordered;

public class AdminBeanDefinitionRegistryPostProcessor extends AbstractToolBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    static InstanceConfig instanceConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) throws BeansException {
        if (instanceConfig != null) {
            BeanDefinition instanceConfigDef = new GenericBeanDefinition();
            Class<?> instanceConfigClass = instanceConfig.getClass();

            instanceConfigDef.setParentName(ToolBeanFactoryUtils.getBeanDefinitionNameOfType((ListableBeanFactory) beanDefReg, beanDefReg, instanceConfigClass,
                true));

            MutablePropertyValues instanceConfigPropValues = instanceConfigDef.getPropertyValues();
            instanceConfigPropValues.add("directory", instanceConfig.getDirectory());
            instanceConfigPropValues.add("domain", instanceConfig.getDomain());

            beanDefReg.registerBeanDefinition("instanceConfigAdmin", instanceConfigDef);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
