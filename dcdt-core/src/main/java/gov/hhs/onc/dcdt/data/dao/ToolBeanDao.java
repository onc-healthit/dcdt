package gov.hhs.onc.dcdt.data.dao;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessor;
import org.springframework.context.ApplicationContextAware;

public interface ToolBeanDao<T extends ToolBean> extends ApplicationContextAware, ToolBeanDataAccessor<T> {
    public boolean hasSession();
}
