package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.AutoStartup;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.service.dns.DnsServiceRuntimeException;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerChannelListenerSelector;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.AsyncListenableTaskExecutor;

@AutoStartup(false)
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
    public synchronized void stop() {
        if (this.isRunning()) {
            this.channelListenerSelector.stop();

            try {
                while (this.isRunning()) {
                    Thread.sleep(DateUtils.MILLIS_PER_SECOND);
                }
            } catch (InterruptedException e) {
                throw new DnsServiceRuntimeException(String.format("DNS server (class=%s, name=%s) thread interrupted while stopping.",
                    ToolClassUtils.getName(this), this.config.getName()), e);
            }

            LOGGER.info(String.format("Stopped DNS server (class=%s, name=%s, bindAddr=%s, bindPort=%d).", ToolClassUtils.getName(this), this.config.getName(),
                this.config.getBindAddress().getHostAddress(), this.config.getBindPort()));
        }
    }

    @Override
    public synchronized void start() {
        if (!this.isRunning()) {
            this.channelListenerSelector = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerChannelListenerSelector.class, this.config);
            // noinspection ConstantConditions
            this.channelListenerSelector.start();

            try {
                while (!this.isRunning()) {
                    Thread.sleep(DateUtils.MILLIS_PER_SECOND);
                }
            } catch (InterruptedException e) {
                throw new DnsServiceRuntimeException(String.format("DNS server (class=%s, name=%s) thread interrupted while starting.",
                    ToolClassUtils.getName(this), this.config.getName()), e);
            }

            LOGGER.info(String.format("Started DNS server (class=%s, name=%s, bindAddr=%s, bindPort=%d).", ToolClassUtils.getName(this), this.config.getName(),
                this.config.getBindAddress().getHostAddress(), this.config.getBindPort()));
        }
    }

    @Override
    public boolean isRunning() {
        return (this.channelListenerSelector != null) && this.channelListenerSelector.isRunning();
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
