package gov.hhs.onc.dcdt.data.events;

import gov.hhs.onc.dcdt.beans.OverrideablePriorityOrdered;
import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolBeanEntityInterceptor<T extends ToolBean> extends OverrideablePriorityOrdered, ToolBeanDataInterceptor {
}
