package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanDefinitionRegistryPostProcessor;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolBeanDefinitionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdminBeanDefinitionRegistryPostProcessor extends AbstractToolBeanDefinitionRegistryPostProcessor {
    private final static String INSTANCE_CONFIG_BEAN_NAME = "instanceConfigAdmin";

    static InstanceConfig instanceConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) throws BeansException {
        if (instanceConfig != null) {
            beanDefReg.registerBeanDefinition(INSTANCE_CONFIG_BEAN_NAME, ToolBeanDefinitionUtils.buildBeanDefinition(beanDefReg, instanceConfig));
        }
    }
}
