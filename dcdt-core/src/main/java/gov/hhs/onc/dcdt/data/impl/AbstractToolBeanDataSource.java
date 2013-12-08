package gov.hhs.onc.dcdt.data.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public abstract class AbstractToolBeanDataSource<T extends ToolBean> extends DriverManagerDataSource implements ToolBeanDataSource<T> {
    protected T bean;
    protected String driverClassName;

    protected void setBean(T bean) {
        this.bean = bean;
    }

    @Override
    public String getDriverClassName() {
        return this.driverClassName;
    }

    @Override
    public void setDriverClassName(String driverClassName) {
        super.setDriverClassName(driverClassName);

        this.driverClassName = driverClassName;
    }
}
