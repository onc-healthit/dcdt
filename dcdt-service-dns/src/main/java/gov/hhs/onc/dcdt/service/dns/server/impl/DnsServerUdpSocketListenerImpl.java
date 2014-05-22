package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractUdpSocketListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketListener;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServerSocketListenerUdp")
@Lazy
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST)
@Scope("prototype")
public class DnsServerUdpSocketListenerImpl extends AbstractUdpSocketListener<DnsServerRequest, DnsServerRequestProcessor> implements
    DnsServerUdpSocketListener {
    private DnsServerConfig serverConfig;

    public DnsServerUdpSocketListenerImpl(DnsServerConfig serverConfig) {
        super(DnsServerRequest.class, DnsServerRequestProcessor.class, serverConfig.toSocketAddress());

        this.serverConfig = serverConfig;
    }

    @Override
    protected DnsServerRequestProcessor createRequestProcessor(Object ... reqProcArgs) {
        return super.createRequestProcessor(ArrayUtils.add(reqProcArgs, 0, this.serverConfig));
    }

    @Override
    @Resource(name = "taskExecServiceDnsServerReqQuery")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
