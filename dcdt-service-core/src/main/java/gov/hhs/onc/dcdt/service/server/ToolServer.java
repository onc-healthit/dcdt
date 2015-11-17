package gov.hhs.onc.dcdt.service.server;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public interface ToolServer<T extends TransportProtocol, U extends ToolServerConfig<T>> extends ApplicationContextAware, ToolLifecycleBean {
    public U getConfig();

    public ThreadPoolTaskExecutor getRequestTaskExecutor();

    public void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec);
}
