package gov.hhs.onc.dcdt.beans.factory;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.PropertySource;

public interface ToolBeanPropertySource<T extends ToolBean> extends InitializingBean, ToolBean {
    public PropertySource<T> toPropertySource() throws ToolBeanException;

    public String getName();

    public void setName();

    public T getSource();

    public void setSource(T source);
}
