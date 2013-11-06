package gov.hhs.onc.dcdt.beans.factory;


import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolBeanPropertyPlaceholderConfigurer extends ToolBean {
    public void setBeanPropertyResolver(ToolBeanPropertyResolver beanPropResolver);
}
