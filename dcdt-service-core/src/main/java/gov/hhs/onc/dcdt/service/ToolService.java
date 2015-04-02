package gov.hhs.onc.dcdt.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

public interface ToolService extends ApplicationContextAware, PriorityOrdered, ToolLifecycleBean {
    @JsonProperty("statusType")
    @Override
    public LifecycleStatusType getLifecycleStatus();
}
