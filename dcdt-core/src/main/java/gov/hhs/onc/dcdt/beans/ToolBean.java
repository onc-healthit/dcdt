package gov.hhs.onc.dcdt.beans;


import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

public interface ToolBean extends BeanFactoryAware, BeanNameAware, InitializingBean {
    public String getBeanName();
}
