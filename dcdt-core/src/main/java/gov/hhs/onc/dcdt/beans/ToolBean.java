package gov.hhs.onc.dcdt.beans;


import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

public interface ToolBean extends BeanFactoryAware, BeanNameAware {
    public String getBeanName();
}
