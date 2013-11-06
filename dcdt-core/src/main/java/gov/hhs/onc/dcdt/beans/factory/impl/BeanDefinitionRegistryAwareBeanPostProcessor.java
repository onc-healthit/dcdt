package gov.hhs.onc.dcdt.beans.factory.impl;


import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("beanDefRegAwareBeanPostProcImpl")
@Scope("singleton")
public class BeanDefinitionRegistryAwareBeanPostProcessor extends AbstractToolBeanPostProcessor<BeanDefinitionRegistryAware> {
    public BeanDefinitionRegistryAwareBeanPostProcessor() {
        super(BeanDefinitionRegistryAware.class);
    }

    @Override
    protected BeanDefinitionRegistryAware postProcessAfterInitializationInternal(BeanDefinitionRegistryAware bean, String beanName) throws ToolBeanException {
        if (!BeanDefinitionRegistry.class.isAssignableFrom(this.beanFactory.getClass())) {
            throw new ToolBeanException("Spring bean factory is not a bean definition registry.");
        }

        bean.setBeanDefinitionRegistry((BeanDefinitionRegistry) this.beanFactory);

        return super.postProcessAfterInitializationInternal(bean, beanName);
    }
}
