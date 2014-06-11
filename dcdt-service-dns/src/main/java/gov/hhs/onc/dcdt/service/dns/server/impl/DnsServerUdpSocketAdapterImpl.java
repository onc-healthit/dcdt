package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.net.sockets.impl.AbstractUdpSocketAdapter;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerUdpSocketAdapter;
import java.net.DatagramSocket;

public class DnsServerUdpSocketAdapterImpl extends AbstractUdpSocketAdapter implements DnsServerUdpSocketAdapter {
    public DnsServerUdpSocketAdapterImpl(DatagramSocket socket) {
        super(socket);
    }
}
