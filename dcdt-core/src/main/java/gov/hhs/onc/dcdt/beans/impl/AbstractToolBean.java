package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

@MappedSuperclass
@Proxy(lazy = false)
public abstract class AbstractToolBean implements ToolBean {
    @Transient
    protected BeanFactory beanFactory;
    
    @Transient
    protected String beanName;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    @Transient
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    @Transient
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
