package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBoundBean;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.ldap.LdapBindCredentialConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import java.util.Objects;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@JsonTypeName("instanceLdapConfig")
public class InstanceLdapConfigImpl extends AbstractToolBoundBean implements InstanceLdapConfig {
    private LdapBindCredentialConfig bindCredConfigAdmin;
    private LdapBindCredentialConfig bindCredConfigAnon;
    private Entry dataPartitionContextEntry;
    private String dataPartitionId;
    private Dn dataPartitionSuffix;
    private String serverId;
    private LdapSslType sslType;

    @Override
    public LdapConnectionConfig toConnectionConfigAdmin() {
        return this.toConnectionConfig(this.bindCredConfigAdmin);
    }

    @Override
    public LdapConnectionConfig toConnectionConfigAnonymous() {
        return this.toConnectionConfig(this.bindCredConfigAnon);
    }

    private LdapConnectionConfig toConnectionConfig(LdapBindCredentialConfig bindCredConfig) {
        LdapConnectionConfig ldapConnConfig = new LdapConnectionConfig();
        ldapConnConfig.setLdapHost(this.bindAddr.getHostAddress());
        ldapConnConfig.setLdapPort(this.bindPort);
        ldapConnConfig.setUseSsl(this.sslType.isSsl());
        ldapConnConfig.setUseTls(this.sslType == LdapSslType.TLS);
        ldapConnConfig.setName(Objects.toString(bindCredConfig.getBindDn(), null));
        ldapConnConfig.setCredentials(bindCredConfig.getBindPassword());

        return ldapConnConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.dataPartitionContextEntry == null) {
            this.dataPartitionContextEntry = new DefaultEntry(this.dataPartitionSuffix);
            this.dataPartitionContextEntry.add(SchemaConstants.OBJECT_CLASS_AT, SchemaConstants.ORGANIZATIONAL_UNIT_OC, SchemaConstants.TOP_OC);
            this.dataPartitionContextEntry.add(this.dataPartitionSuffix.getRdn().getType(), this.dataPartitionSuffix.getRdn().getValue());
        }
    }

    @Override
    public LdapBindCredentialConfig getBindCredentialConfigAdmin() {
        return this.bindCredConfigAdmin;
    }

    @Override
    public void setBindCredentialConfigAdmin(LdapBindCredentialConfig bindCredConfigAdmin) {
        this.bindCredConfigAdmin = bindCredConfigAdmin;
    }

    @Override
    public LdapBindCredentialConfig getBindCredentialConfigAnonymous() {
        return this.bindCredConfigAnon;
    }

    @Override
    public void setBindCredentialConfigAnonymous(LdapBindCredentialConfig bindCredConfigAnon) {
        this.bindCredConfigAnon = bindCredConfigAnon;
    }

    @Override
    public Entry getDataPartitionContextEntry() {
        return this.dataPartitionContextEntry;
    }

    @Override
    public void setDataPartitionContextEntry(Entry dataPartitionContextEntry) {
        this.dataPartitionContextEntry = dataPartitionContextEntry;
    }

    @Override
    public String getDataPartitionId() {
        return this.dataPartitionId;
    }

    @Override
    public void setDataPartitionId(String dataPartitionId) {
        this.dataPartitionId = dataPartitionId;
    }

    public Dn getDataPartitionSuffix() {
        return this.dataPartitionSuffix;
    }

    public void setDataPartitionSuffix(Dn dataPartitionSuffix) {
        this.dataPartitionSuffix = dataPartitionSuffix;
    }

    @Override
    public String getServerId() {
        return this.serverId;
    }

    @Override
    public void setServerId(String serverId) {
        this.serverId = serverId;
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
