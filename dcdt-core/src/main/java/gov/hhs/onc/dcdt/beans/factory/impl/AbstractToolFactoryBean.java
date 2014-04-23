package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.factory.ToolFactoryBean;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
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
    public boolean isEagerInit() {
        try {
            return this.isSingleton() && (this.getEarlySingletonInterfaces() != null);
        } catch (FactoryBeanNotInitializedException ignored) {
            return false;
        }
    }

    @Override
    public boolean isPrototype() {
        return Objects.equals(this.getScopeName(), BeanDefinition.SCOPE_PROTOTYPE);
    }

    @Override
    public boolean isSingleton() {
        String scopeName = this.getScopeName();

        return ((scopeName != null) && Objects.equals(scopeName, BeanDefinition.SCOPE_SINGLETON)) || super.isSingleton();
    }

    @Override
    protected T createInstance() throws Exception {
        return this.createInstanceInternal();
    }

    protected T createInstanceInternal() throws Exception {
        return BeanUtils.instantiateClass(this.beanImplClass, this.beanClass);
    }

    @Nullable
    protected String getScopeName() {
        return ToolAnnotationUtils.getValue(Scope.class, String.class, this.getClass());
    }

    @Override
    public Class<?> getObjectType() {
        return this.beanClass;
    }
}
