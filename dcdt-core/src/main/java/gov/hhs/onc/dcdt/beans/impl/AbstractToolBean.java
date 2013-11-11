package gov.hhs.onc.dcdt.beans.impl;


import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract class AbstractToolBean implements ToolBean {
    protected transient BeanFactory beanFactory;
    protected transient String beanName;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
