package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBoundBean;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.ldap.LdapBindConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import java.util.Objects;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@JsonTypeName("instanceLdapConfig")
public class InstanceLdapConfigImpl extends AbstractToolBoundBean implements InstanceLdapConfig {
    private LdapBindConfig bindConfigAdmin;
    private LdapBindConfig bindConfigAnon;
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
        ldapConnConfig.setLdapHost(this.bindAddr.getHostAddress());
        ldapConnConfig.setLdapPort(this.bindPort);
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
    public LdapSslType getSslType() {
        return this.sslType;
    }

    @Override
    public void setSslType(LdapSslType sslType) {
        this.sslType = sslType;
    }
}
