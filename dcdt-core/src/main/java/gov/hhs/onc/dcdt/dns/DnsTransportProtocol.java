package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.net.SslType;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import javax.annotation.Nonnegative;
import org.xbill.DNS.SimpleResolver;

public enum DnsTransportProtocol implements TransportProtocol {
    DNS("dns", SimpleResolver.DEFAULT_PORT);

    private final String scheme;
    private final int defaultPort;

    private DnsTransportProtocol(String scheme, @Nonnegative int defaultPort) {
        this.scheme = scheme;
        this.defaultPort = defaultPort;
    }

    @Nonnegative
    @Override
    public int getDefaultPort() {
        return this.defaultPort;
    }

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public SslType getSslType() {
        return SslType.NONE;
    }
}
