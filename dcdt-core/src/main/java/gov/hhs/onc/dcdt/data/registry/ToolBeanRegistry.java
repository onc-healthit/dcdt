package gov.hhs.onc.dcdt.data.registry;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import java.util.List;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisherAware;

public interface ToolBeanRegistry<T extends ToolBean, U extends ToolBeanDao<T>, V extends ToolBeanService<T, U>> extends ApplicationContextAware,
    ApplicationEventPublisherAware {
    public void registerAllBeans() throws ToolBeanRegistryException;

    @SuppressWarnings({ "unchecked" })
    public void registerBeans(T ... beans) throws ToolBeanRegistryException;

    public void registerBeans(List<T> beans) throws ToolBeanRegistryException;

    public void removeAllBeans() throws ToolBeanRegistryException;

    @SuppressWarnings({ "unchecked" })
    public void removeBeans(T ... beans) throws ToolBeanRegistryException;

    public void removeBeans(List<T> beans) throws ToolBeanRegistryException;
}
