package gov.hhs.onc.dcdt.service.ldap.conf;


import org.apache.directory.server.core.authn.AnonymousAuthenticator;
import org.apache.directory.server.core.authn.Authenticator;

/**
 * @see org.apache.directory.server.core.annotations.CreateAuthenticator
 */
public class AuthenticatorConfig {
    private Class<? extends Authenticator> type = AnonymousAuthenticator.class;
    private String delegateHost = "localhost";
    private int delegatePort = -1;
    private String delegateBaseDn = "";
    private boolean delegateSsl = false;
    private boolean delegateTls = false;
    private String delegateSslTrustManagerFQCN = "org.apache.directory.ldap.client.api.NoVerificationTrustManager";
    private String delegateTlsTrustManagerFQCN = "org.apache.directory.ldap.client.api.NoVerificationTrustManager";

    public String getDelegateBaseDn() {
        return this.delegateBaseDn;
    }

    public void setDelegateBaseDn(String delegateBaseDn) {
        this.delegateBaseDn = delegateBaseDn;
    }

    public String getDelegateHost() {
        return this.delegateHost;
    }

    public void setDelegateHost(String delegateHost) {
        this.delegateHost = delegateHost;
    }

    public int getDelegatePort() {
        return this.delegatePort;
    }

    public void setDelegatePort(int delegatePort) {
        this.delegatePort = delegatePort;
    }

    public boolean isDelegateSsl() {
        return this.delegateSsl;
    }

    public void setDelegateSsl(boolean delegateSsl) {
        this.delegateSsl = delegateSsl;
    }

    public String getDelegateSslTrustManagerFQCN() {
        return this.delegateSslTrustManagerFQCN;
    }

    public void setDelegateSslTrustManagerFQCN(String delegateSslTrustManagerFQCN) {
        this.delegateSslTrustManagerFQCN = delegateSslTrustManagerFQCN;
    }

    public boolean isDelegateTls() {
        return this.delegateTls;
    }

    public void setDelegateTls(boolean delegateTls) {
        this.delegateTls = delegateTls;
    }

    public String getDelegateTlsTrustManagerFQCN() {
        return this.delegateTlsTrustManagerFQCN;
    }

    public void setDelegateTlsTrustManagerFQCN(String delegateTlsTrustManagerFQCN) {
        this.delegateTlsTrustManagerFQCN = delegateTlsTrustManagerFQCN;
    }

    public Class<? extends Authenticator> getType() {
        return this.type;
    }

    public void setType(Class<? extends Authenticator> type) {
        this.type = type;
    }
}
