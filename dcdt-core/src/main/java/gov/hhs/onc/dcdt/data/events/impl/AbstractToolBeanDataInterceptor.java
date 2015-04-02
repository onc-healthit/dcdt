package gov.hhs.onc.dcdt.data.events.impl;

import gov.hhs.onc.dcdt.data.events.ToolBeanDataInterceptor;
import org.hibernate.EmptyInterceptor;

public abstract class AbstractToolBeanDataInterceptor extends EmptyInterceptor implements ToolBeanDataInterceptor {
    protected final static long serialVersionUID = 0L;
}
