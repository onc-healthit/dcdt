package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.net.sockets.SocketRequest;
import javax.annotation.Nonnegative;

public interface DnsServerRequest extends SocketRequest {
    public boolean hasQuerySize();

    public int getQuerySize();

    @Nonnegative
    public int setQuerySize(@Nonnegative int querySize);
}
