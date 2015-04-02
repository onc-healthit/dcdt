package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractTcpSocketListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpServerSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpSocketListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServerSocketListenerTcp")
@Lazy
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST)
@Scope("prototype")
public class DnsServerTcpSocketListenerImpl extends
    AbstractTcpSocketListener<DnsServerTcpServerSocketAdapter, DnsServerTcpSocketAdapter, DnsServerRequest, DnsServerRequestProcessor> implements
    DnsServerTcpSocketListener {
    private DnsServerConfig serverConfig;

    public DnsServerTcpSocketListenerImpl(DnsServerConfig serverConfig) {
        super(DnsServerTcpServerSocketAdapter.class, DnsServerTcpSocketAdapter.class, DnsServerRequest.class, DnsServerRequestProcessor.class, serverConfig
            .toSocketAddress());

        this.serverConfig = serverConfig;
    }

    @Override
    protected DnsServerRequestProcessor createRequestProcessor(Object ... reqProcArgs) {
        return super.createRequestProcessor(ArrayUtils.add(reqProcArgs, 0, this.serverConfig));
    }

    @Override
    protected DnsServerTcpSocketAdapter readRequest(DnsServerTcpServerSocketAdapter listenSocketAdapter, DnsServerRequest req) throws IOException {
        DnsServerTcpSocketAdapter reqSocketAdapter = super.readRequest(listenSocketAdapter, req);
        ByteBuffer reqBuffer = req.getRequestBuffer();
        int reqQuerySize = req.setQuerySize(ToolDnsMessageUtils.parseQuerySizeData(reqBuffer.get(0), reqBuffer.get(1)));

        reqBuffer.limit(reqBuffer.limit() + reqQuerySize);
        reqBuffer.put(reqSocketAdapter.read(reqQuerySize).getRight());

        return reqSocketAdapter;
    }

    @Override
    protected DnsServerRequest createRequest(InetProtocol protocol) {
        DnsServerRequest req = super.createRequest(protocol);
        req.getRequestBuffer().limit(ToolDnsMessageUtils.DATA_SIZE_DNS_MSG_QUERY_SIZE_PREFIX);

        return req;
    }

    @Override
    @Resource(name = "taskExecServiceDnsServerReq")
    protected void setRequestTaskExecutor(ThreadPoolTaskExecutor reqTaskExec) {
        super.setRequestTaskExecutor(reqTaskExec);
    }

    @Override
    @Resource(name = "taskExecServiceDnsServer")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
