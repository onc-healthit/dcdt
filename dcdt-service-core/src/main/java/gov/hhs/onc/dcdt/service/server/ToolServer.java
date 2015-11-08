package gov.hhs.onc.dcdt.service.server;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import org.springframework.context.ApplicationContextAware;

@AutoStartup(false)
public interface ToolServer<T extends ToolServerConfig> extends ApplicationContextAware, ToolLifecycleBean {
    public T getConfig();
}
