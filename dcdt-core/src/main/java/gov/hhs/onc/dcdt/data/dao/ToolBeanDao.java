package gov.hhs.onc.dcdt.data.dao;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessor;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

public interface ToolBeanDao<T extends ToolBean> extends BeanDefinitionRegistryAware, BeanFactoryAware, InitializingBean, ToolBeanDataAccessor<T> {
    public boolean hasSession();
}
