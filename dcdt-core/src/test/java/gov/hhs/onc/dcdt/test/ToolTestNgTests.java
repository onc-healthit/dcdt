package gov.hhs.onc.dcdt.test;


import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

public abstract class ToolTestNgTests extends AbstractTestNGSpringContextTests {
    protected <T> T autowire(T bean) {
        this.getBeanFactory().autowireBean(bean);

        return bean;
    }

    protected <T> T autowire(Class<T> beanClass) {
        return beanClass.cast(this.getBeanFactory().autowire(beanClass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false));
    }

    protected AutowireCapableBeanFactory getBeanFactory() {
        return this.applicationContext.getAutowireCapableBeanFactory();
    }
}
