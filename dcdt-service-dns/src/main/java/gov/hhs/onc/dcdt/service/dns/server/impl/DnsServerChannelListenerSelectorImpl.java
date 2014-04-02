package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.nio.channels.impl.AbstractChannelListenerSelector;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerChannelListenerSelector;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServerTcpChannelListenerSelectorImpl")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerChannelListenerSelectorImpl extends AbstractChannelListenerSelector implements DnsServerChannelListenerSelector {
    private AbstractApplicationContext appContext;
    private DnsServerConfig dnsServerConfig;

    public DnsServerChannelListenerSelectorImpl(DnsServerConfig dnsServerConfig) {
        this.dnsServerConfig = dnsServerConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setChannelListeners(ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerUdpChannelListener.class, this.dnsServerConfig),
            ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerTcpChannelListener.class, this.dnsServerConfig));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    @Resource(name = "taskExecServiceDnsServerChannel")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
