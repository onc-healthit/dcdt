package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpSocketListener;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketListener;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolServer;
import javax.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Phase(Phase.PHASE_PRECEDENCE_HIGHEST)
public class DnsServerImpl extends AbstractToolServer<DnsServerConfig> implements DnsServer {
    private DnsServerUdpSocketListener udpSocketListener;
    private DnsServerTcpSocketListener tcpSocketListener;

    public DnsServerImpl(DnsServerConfig config) {
        super(config);
    }

    @Override
    public boolean isRunning() {
        return (super.isRunning() && (((this.udpSocketListener != null) && this.udpSocketListener.isRunning()) || (((this.tcpSocketListener != null) && this.tcpSocketListener
            .isRunning()))));
    }

    @Override
    protected void stopInternal() throws Exception {
        if (this.udpSocketListener != null) {
            this.udpSocketListener.stop();
        }

        if (this.tcpSocketListener != null) {
            this.tcpSocketListener.stop();
        }

        super.stopInternal();
    }

    @Override
    protected void startInternal() throws Exception {
        this.udpSocketListener = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerUdpSocketListener.class, this.config);
        // noinspection ConstantConditions
        this.udpSocketListener.start();

        this.tcpSocketListener = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsServerTcpSocketListener.class, this.config);
        // noinspection ConstantConditions
        this.tcpSocketListener.start();

        super.startInternal();
    }

    @Override
    @Resource(name = "taskExecServiceDnsServer")
    protected void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
