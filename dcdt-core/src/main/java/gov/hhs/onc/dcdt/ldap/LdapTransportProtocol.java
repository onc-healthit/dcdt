package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.net.SslType;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import javax.annotation.Nonnegative;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public enum LdapTransportProtocol implements TransportProtocol {
    LDAP("ldap", LdapConnectionConfig.DEFAULT_LDAP_PORT, SslType.NONE), LDAPS("ldaps", LdapConnectionConfig.DEFAULT_LDAPS_PORT, SslType.SSL), LDAP_STARTTLS(
        "ldap", LdapConnectionConfig.DEFAULT_LDAP_PORT, SslType.STARTTLS);

    private final String scheme;
    private final int defaultPort;
    private final SslType sslType;

    private LdapTransportProtocol(String scheme, @Nonnegative int defaultPort, SslType sslType) {
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
