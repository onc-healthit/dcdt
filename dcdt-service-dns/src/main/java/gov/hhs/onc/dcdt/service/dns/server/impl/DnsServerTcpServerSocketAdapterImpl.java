package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.net.sockets.impl.AbstractTcpServerSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpServerSocketAdapter;
import java.net.ServerSocket;

public class DnsServerTcpServerSocketAdapterImpl extends AbstractTcpServerSocketAdapter implements DnsServerTcpServerSocketAdapter {
    public DnsServerTcpServerSocketAdapterImpl(ServerSocket socket) {
        super(socket);
    }
}
