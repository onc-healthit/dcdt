package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.ldap.LdapBindConfig;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import java.util.Objects;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.Name;

public class InstanceLdapConfigImpl extends AbstractToolNamedBean implements InstanceLdapConfig {
    private LdapBindConfig bindConfigAdmin;
    private LdapBindConfig bindConfigAnon;
    private Name host;
    private int port;
    private LdapSslType sslType;

    @Override
    public LdapConnectionConfig toConnectionConfigAdmin() {
        return this.toConnectionConfig(this.bindConfigAdmin);
    }

    @Override
    public LdapConnectionConfig toConnectionConfigAnonymous() {
        return this.toConnectionConfig(this.bindConfigAnon);
    }

    private LdapConnectionConfig toConnectionConfig(LdapBindConfig bindConfig) {
        LdapConnectionConfig ldapConnConfig = new LdapConnectionConfig();
        ldapConnConfig.setLdapHost(this.host.toString());
        ldapConnConfig.setLdapPort(this.port);
        ldapConnConfig.setUseSsl(this.sslType.isSsl());
        ldapConnConfig.setUseTls(this.sslType == LdapSslType.TLS);
        ldapConnConfig.setName(Objects.toString(bindConfig.getBindDn(), null));
        ldapConnConfig.setCredentials(bindConfig.getBindPassword());

        return ldapConnConfig;
    }

    @Override
    public LdapBindConfig getBindConfigAdmin() {
        return this.bindConfigAdmin;
    }

    @Override
    public void setBindConfigAdmin(LdapBindConfig bindConfigAdmin) {
        this.bindConfigAdmin = bindConfigAdmin;
    }

    @Override
    public LdapBindConfig getBindConfigAnonymous() {
        return this.bindConfigAnon;
    }

    @Override
    public void setBindConfigAnonymous(LdapBindConfig bindConfigAnon) {
        this.bindConfigAnon = bindConfigAnon;
    }

    @Override
    public Name getHost() {
        return this.host;
    }

    @Override
    public void setHost(Name host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public LdapSslType getSslType() {
        return this.sslType;
    }

    @Override
    public void setSslType(LdapSslType sslType) {
        this.sslType = sslType;
    }
}
