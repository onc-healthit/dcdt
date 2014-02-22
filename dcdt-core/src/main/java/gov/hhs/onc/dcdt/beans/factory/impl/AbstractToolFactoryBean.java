package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.factory.ToolFactoryBean;
import javax.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractToolFactoryBean<T> extends AbstractFactoryBean<T> implements ToolFactoryBean<T> {
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractToolFactoryBean(Class<T> beanClass) {
        this(beanClass, null);
    }

    protected AbstractToolFactoryBean(Class<T> beanClass, @Nullable Class<? extends T> beanImplClass) {
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    protected T createInstance() throws Exception {
        return BeanUtils.instantiateClass(this.beanImplClass, this.beanClass);
    }

    @Override
    public Class<?> getObjectType() {
        return this.beanClass;
    }
}
