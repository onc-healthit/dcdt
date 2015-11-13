package gov.hhs.onc.dcdt.service.server.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public abstract class AbstractToolServer<T extends ToolServerConfig> extends AbstractToolLifecycleBean implements ToolServer<T> {
    protected T config;
    protected AbstractApplicationContext appContext;
    protected ThreadPoolTaskExecutor reqTaskExec;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolServer.class);

    protected AbstractToolServer(T config) {
        this.config = config;
    }

    @Override
    protected void stopInternal() throws Exception {
        LOGGER.info(String.format("Stopped %s server (class=%s, name=%s, bindSocketAddr={%s}).", this.config.getProtocol(), ToolClassUtils.getName(this),
            this.config.getName(), this.config.toSocketAddress()));
    }

    @Override
    protected void startInternal() throws Exception {
        LOGGER.info(String.format("Started %s server (class=%s, name=%s, bindSocketAddr={%s}).", this.config.getProtocol(), ToolClassUtils.getName(this),
            this.config.getName(), this.config.toSocketAddress()));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public T getConfig() {
        return this.config;
    }

    @Override
    public ThreadPoolTaskExecutor getRequestTaskExecutor() {
        return this.reqTaskExec;
    }

    @Override
    public void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec) {
        this.reqTaskExec = reqTaskExec;
    }
}
