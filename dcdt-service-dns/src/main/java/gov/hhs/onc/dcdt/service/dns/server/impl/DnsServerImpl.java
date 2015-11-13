package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpSocketListener;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketListener;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolServer;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
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
        this.tcpSocketListener.stop();
        this.udpSocketListener.stop();

        super.stopInternal();
    }

    @Override
    protected void startInternal() throws Exception {
        this.tcpSocketListener.start();
        this.udpSocketListener.start();

        super.startInternal();
    }

    @Override
    public DnsServerTcpSocketListener getTcpSocketListener() {
        return this.tcpSocketListener;
    }

    @Override
    public void setTcpSocketListener(DnsServerTcpSocketListener tcpSocketListener) {
        this.tcpSocketListener = tcpSocketListener;
    }

    @Override
    public DnsServerUdpSocketListener getUdpSocketListener() {
        return this.udpSocketListener;
    }

    @Override
    public void setUdpSocketListener(DnsServerUdpSocketListener udpSocketListener) {
        this.udpSocketListener = udpSocketListener;
    }
}
