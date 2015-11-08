package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractUdpSocketListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketListener;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServerSocketListenerUdp")
@Lazy
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST)
@Scope("prototype")
public class DnsServerUdpSocketListenerImpl extends AbstractUdpSocketListener<DnsServerUdpSocketAdapter, DnsServerRequest, DnsServerRequestProcessor> implements
    DnsServerUdpSocketListener {
    private DnsServerConfig serverConfig;

    public DnsServerUdpSocketListenerImpl(DnsServerConfig serverConfig) {
        super(DnsServerUdpSocketAdapter.class, DnsServerRequest.class, DnsServerRequestProcessor.class, serverConfig.toSocketAddress());

        this.serverConfig = serverConfig;
    }

    @Override
    protected DnsServerRequestProcessor createRequestProcessor(Object ... reqProcArgs) {
        return super.createRequestProcessor(ArrayUtils.add(reqProcArgs, 0, this.serverConfig));
    }

    @Override
    @Resource(name = "taskExecServiceDnsServerReq")
    protected void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec) {
        super.setRequestTaskExecutor(reqTaskExec);
    }

    @Override
    @Resource(name = "taskExecServiceDnsServer")
    protected void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
