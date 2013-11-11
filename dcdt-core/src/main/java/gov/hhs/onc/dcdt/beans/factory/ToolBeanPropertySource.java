package gov.hhs.onc.dcdt.beans.factory;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanException;
import org.springframework.core.env.PropertySource;

public interface ToolBeanPropertySource<T extends ToolBean> extends ToolBean {
    public PropertySource<T> toPropertySource() throws ToolBeanException;

    public boolean hasName();

    public String getName();

    public void setName();

    public boolean hasSource();

    public T getSource();

    public void setSource(T source);
}
