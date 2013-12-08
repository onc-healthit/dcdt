package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertyPlaceholderConfigurer;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertyResolver;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;

public class ToolBeanPropertyPlaceholderConfigurerImpl extends PropertySourcesPlaceholderConfigurer implements ToolBeanPropertyPlaceholderConfigurer {
    private String beanName;
    private AutowireCapableBeanFactory beanFactory;
    private ToolBeanPropertyResolver beanPropResolver;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propResolver) throws BeansException {
        Map<String, InstanceConfig> instanceBeans = ToolBeanFactoryUtils.mapBeansOfType(beanFactoryToProcess, InstanceConfig.class);

        if (!instanceBeans.isEmpty()) {
            if (beanFactoryToProcess.equals(this.beanFactory)) {
                BeanDefinitionVisitor beanDefVisitor = new BeanDefinitionVisitor(this.beanPropResolver);
                BeanDefinition beanDef;

                for (String beanDefName : beanFactoryToProcess.getBeanDefinitionNames()) {
                    if (!beanDefName.equals(this.beanName) && !instanceBeans.containsKey(beanDefName)) {
                        beanDef = beanFactoryToProcess.getBeanDefinition(beanDefName);

                        try {
                            beanDefVisitor.visitBeanDefinition(beanDef);
                        } catch (Exception e) {
                            throw new BeanDefinitionStoreException(beanDef.getResourceDescription(), beanDefName, e);
                        }
                    }
                }
            }

            beanFactoryToProcess.resolveAliases(this.beanPropResolver);
            beanFactoryToProcess.addEmbeddedValueResolver(this.beanPropResolver);
        }

        super.processProperties(beanFactoryToProcess, propResolver);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;

        super.setBeanFactory(beanFactory);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;

        super.setBeanName(beanName);
    }

    @Override
    public void setBeanPropertyResolver(ToolBeanPropertyResolver beanPropResolver) {
        this.beanPropResolver = beanPropResolver;
    }
}
