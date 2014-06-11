package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.net.sockets.UdpSocketListener;

public interface DnsServerUdpSocketListener extends UdpSocketListener<DnsServerUdpSocketAdapter, DnsServerRequest, DnsServerRequestProcessor> {
}
