package gov.hhs.onc.dcdt.service;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

public interface ToolService extends ApplicationContextAware, PriorityOrdered, ToolLifecycleBean {
}
