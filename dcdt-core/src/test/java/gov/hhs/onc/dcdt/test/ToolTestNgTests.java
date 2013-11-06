package gov.hhs.onc.dcdt.test;

import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(loader = ToolTestNgContextLoader.class, locations = { "spring/spring-core*.xml" })
public abstract class ToolTestNgTests extends AbstractTestNGSpringContextTests implements BeanDefinitionRegistryAware {
    protected BeanDefinitionRegistry beanDefReg;

    @Override
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        this.beanDefReg = beanDefReg;
    }

    protected AutowireCapableBeanFactory getBeanFactory() {
        return this.getApplicationContext().getAutowireCapableBeanFactory();
    }

    protected AbstractApplicationContext getApplicationContext() {
        return (AbstractApplicationContext) this.applicationContext;
    }
}
