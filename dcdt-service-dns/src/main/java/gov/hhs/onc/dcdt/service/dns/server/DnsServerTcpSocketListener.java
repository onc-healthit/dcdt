package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.net.sockets.TcpSocketListener;

public interface DnsServerTcpSocketListener extends TcpSocketListener<DnsServerRequest, DnsServerRequestProcessor> {
}
