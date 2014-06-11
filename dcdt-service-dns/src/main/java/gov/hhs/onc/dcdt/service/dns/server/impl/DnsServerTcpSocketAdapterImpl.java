package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.net.sockets.impl.AbstractTcpSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerTcpSocketAdapter;
import java.net.Socket;

public class DnsServerTcpSocketAdapterImpl extends AbstractTcpSocketAdapter implements DnsServerTcpSocketAdapter {
    public DnsServerTcpSocketAdapterImpl(Socket socket) {
        super(socket);
    }
}
