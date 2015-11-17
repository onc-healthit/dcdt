package gov.hhs.onc.dcdt.http;

import gov.hhs.onc.dcdt.net.SslType;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import javax.annotation.Nonnegative;

public enum HttpTransportProtocol implements TransportProtocol {
    HTTP("http", 80, SslType.NONE), HTTPS("https", 443, SslType.SSL);

    private final String scheme;
    private final int defaultPort;
    private final SslType sslType;

    private HttpTransportProtocol(String scheme, @Nonnegative int defaultPort, SslType sslType) {
        this.scheme = scheme;
        this.defaultPort = defaultPort;
        this.sslType = sslType;
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
        return this.sslType;
    }
}
