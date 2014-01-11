package gov.hhs.onc.dcdt.data.registry;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import org.springframework.context.ApplicationContextAware;

public interface ToolBeanRegistry<T extends ToolBean, U extends ToolBeanDao<T>, V extends ToolBeanService<T, U>> extends ApplicationContextAware {
    public void processBeans() throws ToolBeanRegistryException;
}
