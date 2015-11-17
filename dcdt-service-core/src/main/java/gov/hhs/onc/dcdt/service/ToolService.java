package gov.hhs.onc.dcdt.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import java.util.List;
import org.springframework.context.ApplicationContextAware;

public interface ToolService<T extends TransportProtocol, U extends ToolServerConfig<T>, V extends ToolServer<T, U>> extends ApplicationContextAware,
    ToolLifecycleBean {
    @JsonProperty("statusType")
    @Override
    public LifecycleStatusType getLifecycleStatus();

    public boolean hasServers();

    public List<V> getServers();

    public void setServers(List<V> servers);
}
