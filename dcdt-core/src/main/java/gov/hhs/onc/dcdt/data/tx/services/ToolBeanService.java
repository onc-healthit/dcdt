package gov.hhs.onc.dcdt.data.tx.services;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessor;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;

public interface ToolBeanService<T extends ToolBean, U extends ToolBeanDao<T>> extends ToolBeanDataAccessor<T> {
}
