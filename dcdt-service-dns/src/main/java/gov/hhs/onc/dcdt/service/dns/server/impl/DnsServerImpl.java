package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerChannelListenerSelector;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.AsyncListenableTaskExecutor;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST)
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerImpl extends AbstractToolLifecycleBean implements DnsServer {
    private AbstractApplicationContext appContext;
    private DnsServerConfig config;
    private DnsServerChannelListenerSelector channelListenerSelector;

    private final static Logger LOGGER = LoggerFactory.getLogger(DnsServerImpl.class);

    public DnsServerImpl(DnsServerConfig config) {
        this.config = config;
    }

    @Override
    public boolean isRunning() {
        return super.isRunning() && (this.channelListenerSelector != null) && this.channelListenerSelector.isRunning();
    }

    @Override
    protected void stopInternal() throws Exception {
        this.channelListenerSelector.stop();

        LOGGER.info(String.format("Stopped DNS server (class=%s, name=%s, host=%s, port=%d).", ToolClassUtils.getName(this), this.config.getName(),
            this.config.getHost(), this.config.getPort()));
    }

    @Override
    protected void startInternal() throws Exception {
        this.channelListenerSelector = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerChannelListenerSelector.class, this.config);
        // noinspection ConstantConditions
        this.channelListenerSelector.start();

        LOGGER.info(String.format("Started DNS server (class=%s, name=%s, host=%s, port=%d).", ToolClassUtils.getName(this), this.config.getName(),
            this.config.getHost(), this.config.getPort()));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public DnsServerConfig getConfig() {
        return this.config;
    }

    @Override
    @Resource(name = "taskExecServiceDnsServer")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
