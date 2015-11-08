package gov.hhs.onc.dcdt.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import java.util.List;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

@AutoStartup(false)
@ServiceContextConfiguration({ "spring/spring-core.xml", "spring/spring-core*.xml", "spring/spring-service.xml", "spring/spring-service-embedded.xml",
    "spring/spring-service-standalone.xml" })
public interface ToolService<T extends ToolServerConfig, U extends ToolServer<T>> extends ApplicationContextAware, PriorityOrdered, ToolLifecycleBean {
    @JsonProperty("statusType")
    @Override
    public LifecycleStatusType getLifecycleStatus();

    public boolean hasServers();

    public List<U> getServers();

    public void setServers(List<U> servers);
}
