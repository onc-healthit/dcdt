package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("beanDefRegAwareBeanPostProc")
@Scope("singleton")
public class BeanDefinitionRegistryAwareBeanPostProcessor extends AbstractToolBeanPostProcessor<BeanDefinitionRegistryAware> {
    public BeanDefinitionRegistryAwareBeanPostProcessor() {
        super(BeanDefinitionRegistryAware.class);
    }

    @Override
    public boolean canPostProcessBeforeInitialization(BeanDefinitionRegistryAware bean, String beanName) {
        return true;
    }

    @Override
    protected BeanDefinitionRegistryAware postProcessBeforeInitializationInternal(BeanDefinitionRegistryAware bean, String beanName) throws ToolBeanException {
        if (ToolClassUtils.isAssignable(BeanDefinitionRegistry.class, this.appContext.getClass())) {
            bean.setBeanDefinitionRegistry((BeanDefinitionRegistry) this.appContext);
        } else {
            AutowireCapableBeanFactory beanFactory;

            if (ToolClassUtils.isAssignable(BeanDefinitionRegistry.class, (beanFactory = this.appContext.getAutowireCapableBeanFactory()).getClass())) {
                bean.setBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory);
            } else {
                throw new ToolBeanException("Unable to determine Spring bean definition registry.");
            }
        }

        return super.postProcessBeforeInitializationInternal(bean, beanName);
    }
}
