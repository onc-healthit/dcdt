package gov.hhs.onc.dcdt.ldap;

import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.url.LdapUrl;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public enum LdapSslType {
    NONE(false, null, LdapUrl.LDAP_SCHEME), TLS(true, LdapConnectionConfig.DEFAULT_SSL_PROTOCOL, LdapUrl.LDAPS_SCHEME);

    private final boolean ssl;
    private final String sslProtocol;
    private final String urlScheme;

    private LdapSslType(boolean ssl, @Nullable String sslProtocol, String urlScheme) {
        this.ssl = ssl;
        this.sslProtocol = sslProtocol;
        this.urlScheme = urlScheme;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    @Nullable
    public String getSslProtocol() {
        return this.sslProtocol;
    }

    public String getUrlScheme() {
        return this.urlScheme;
    }
}
