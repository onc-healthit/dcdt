package gov.hhs.onc.dcdt.service.server;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public interface ToolServer<T extends ToolServerConfig> extends ApplicationContextAware, ToolLifecycleBean {
    public T getConfig();

    public ThreadPoolTaskExecutor getRequestTaskExecutor();

    public void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec);
}
