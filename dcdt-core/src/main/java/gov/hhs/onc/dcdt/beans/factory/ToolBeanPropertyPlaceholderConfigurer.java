package gov.hhs.onc.dcdt.beans.factory;


import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

public interface ToolBeanPropertyPlaceholderConfigurer extends BeanFactoryAware, BeanNameAware {
    public void setBeanPropertyResolver(ToolBeanPropertyResolver beanPropResolver);
}
