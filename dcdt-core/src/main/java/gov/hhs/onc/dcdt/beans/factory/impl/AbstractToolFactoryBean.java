package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.factory.ToolFactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractToolFactoryBean<T> extends AbstractFactoryBean<T> implements ToolFactoryBean<T> {
    protected Class<T> beanClass;

    protected AbstractToolFactoryBean(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Class<?> getObjectType() {
        return this.beanClass;
    }
}
