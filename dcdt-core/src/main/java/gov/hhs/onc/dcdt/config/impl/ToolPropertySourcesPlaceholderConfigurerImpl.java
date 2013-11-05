package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.config.ToolBeanPropertySourcesPropertyResolver;
import gov.hhs.onc.dcdt.config.ToolInstance;
import gov.hhs.onc.dcdt.config.ToolPropertySourcesPlaceholderConfigurer;
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

public class ToolPropertySourcesPlaceholderConfigurerImpl extends PropertySourcesPlaceholderConfigurer implements ToolPropertySourcesPlaceholderConfigurer {
    private String beanName;
    private AutowireCapableBeanFactory beanFactory;
    private ToolBeanPropertySourcesPropertyResolver propSourcesPropResolver;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propResolver) throws BeansException {
        if (beanFactoryToProcess.equals(this.beanFactory)) {
            Map<String, ToolInstance> instanceBeans = beanFactoryToProcess.getBeansOfType(ToolInstance.class);

            if (!instanceBeans.isEmpty()) {
                BeanDefinitionVisitor beanDefVisitor = new BeanDefinitionVisitor(this.propSourcesPropResolver);
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
    public void setBeanName(String beanName) {
        this.beanName = beanName;

        super.setBeanName(beanName);
    }

    @Override
    public void setPropertySourcesPropertyResolver(ToolBeanPropertySourcesPropertyResolver propSourcesPropResolver) {
        this.propSourcesPropResolver = propSourcesPropResolver;
    }
}
