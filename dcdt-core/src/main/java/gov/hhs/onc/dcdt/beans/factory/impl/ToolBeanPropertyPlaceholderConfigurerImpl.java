package gov.hhs.onc.dcdt.beans.factory.impl;


import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertyPlaceholderConfigurer;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertyResolver;
import gov.hhs.onc.dcdt.config.ToolInstance;
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
    private ToolBeanPropertyResolver beanPropResolver;

    private String beanName;
    private AutowireCapableBeanFactory beanFactory;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propResolver) throws BeansException {
        if (beanFactoryToProcess.equals(this.beanFactory)) {
            Map<String, ToolInstance> instanceBeans = ToolBeanFactoryUtils.tryGetBeansOfType(beanFactoryToProcess, ToolInstance.class);

            if (!instanceBeans.isEmpty()) {
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
        }

        super.processProperties(beanFactoryToProcess, propResolver);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;

        super.setBeanFactory(beanFactory);
    }

    @Override
    public String getBeanName() {
        return this.beanName;
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
