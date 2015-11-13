package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractUdpSocketListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketListener;
import org.apache.commons.lang3.ArrayUtils;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
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
}
