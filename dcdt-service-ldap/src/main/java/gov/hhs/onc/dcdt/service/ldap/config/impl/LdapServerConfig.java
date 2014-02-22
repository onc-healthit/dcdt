package gov.hhs.onc.dcdt.service.ldap.config.impl;

import gov.hhs.onc.dcdt.service.ldap.factory.impl.ToolLdapServerFactory;
import org.apache.directory.server.factory.LdapServerFactory;

/**
 * @see org.apache.directory.server.annotations.CreateLdapServer
 */
public class LdapServerConfig {
    private String name = "ldapServerDefault";
    private TransportConfig[] transports;
    private Class<? extends LdapServerFactory> factoryClass = ToolLdapServerFactory.class;
    private long maxSizeLimit = 1000;
    private int maxTimeLimit = 1000;
    private boolean allowAnonymousAccess = true;
    private String keyStore = "";
    private String certificatePassword = "";
    private Class<?>[] extendedOpHandlers;
    private SaslMechanismConfig[] saslMechanisms;
    private Class<?> ntlmProvider = Object.class;
    private String saslHost = "ldap.example.com";
    private String[] saslRealms = { "example.com" };
    private String saslPrincipal = "ldap/ldap.example.com@EXAMPLE.COM";

    public boolean isAllowAnonymousAccess() {
        return this.allowAnonymousAccess;
    }

    public void setAllowAnonymousAccess(boolean allowAnonymousAccess) {
        this.allowAnonymousAccess = allowAnonymousAccess;
    }

    public String getCertificatePassword() {
        return this.certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public Class<?>[] getExtendedOpHandlers() {
        return this.extendedOpHandlers;
    }

    public void setExtendedOpHandlers(Class<?>[] extendedOpHandlers) {
        this.extendedOpHandlers = extendedOpHandlers;
    }

    public Class<? extends LdapServerFactory> getFactoryClass() {
        return this.factoryClass;
    }

    public void setFactoryClass(Class<? extends LdapServerFactory> factoryClass) {
        this.factoryClass = factoryClass;
    }

    public String getKeyStore() {
        return this.keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public long getMaxSizeLimit() {
        return this.maxSizeLimit;
    }

    public void setMaxSizeLimit(long maxSizeLimit) {
        this.maxSizeLimit = maxSizeLimit;
    }

    public int getMaxTimeLimit() {
        return this.maxTimeLimit;
    }

    public void setMaxTimeLimit(int maxTimeLimit) {
        this.maxTimeLimit = maxTimeLimit;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getNtlmProvider() {
        return this.ntlmProvider;
    }

    public void setNtlmProvider(Class<?> ntlmProvider) {
        this.ntlmProvider = ntlmProvider;
    }

    public String getSaslHost() {
        return this.saslHost;
    }

    public void setSaslHost(String saslHost) {
        this.saslHost = saslHost;
    }

    public SaslMechanismConfig[] getSaslMechanisms() {
        return this.saslMechanisms;
    }

    public void setSaslMechanisms(SaslMechanismConfig[] saslMechanisms) {
        this.saslMechanisms = saslMechanisms;
    }

    public String getSaslPrincipal() {
        return this.saslPrincipal;
    }

    public void setSaslPrincipal(String saslPrincipal) {
        this.saslPrincipal = saslPrincipal;
    }

    public String[] getSaslRealms() {
        return this.saslRealms;
    }

    public void setSaslRealms(String[] saslRealms) {
        this.saslRealms = saslRealms;
    }

    public TransportConfig[] getTransports() {
        return this.transports;
    }

    public void setTransports(TransportConfig[] transports) {
        this.transports = transports;
    }
}
