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
import org.apache.directory.api.ldap.model.name.Rdn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@JsonTypeName("instanceLdapConfig")
public class InstanceLdapConfigImpl extends AbstractToolBoundBean implements InstanceLdapConfig {
    private final static String ATTR_VALUE_OU_DCDT = "dcdt";

    private LdapBindCredentialConfig bindCredConfigAdmin;
    private LdapBindCredentialConfig bindCredConfigAnon;
    private Entry partitionContextEntry;
    private String partitionId;
    private Dn partitionSuffix;
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
        this.serverId = ((this.serverId != null) ? serverId : this.name);
        this.partitionId = ((this.partitionId != null) ? partitionId : this.name);
        this.partitionSuffix =
            ((this.partitionSuffix != null) ? partitionSuffix : new Dn(new Rdn(SchemaConstants.OU_AT, this.serverId), new Rdn(SchemaConstants.OU_AT,
                ATTR_VALUE_OU_DCDT)));

        if (this.partitionContextEntry == null) {
            this.partitionContextEntry = new DefaultEntry(this.partitionSuffix);
            this.partitionContextEntry.add(SchemaConstants.OBJECT_CLASS_AT, SchemaConstants.TOP_OC, SchemaConstants.ORGANIZATIONAL_UNIT_OC);
            this.partitionContextEntry.add(this.partitionSuffix.getRdn().getType(), this.partitionSuffix.getRdn().getValue());
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
    public Entry getPartitionContextEntry() {
        return this.partitionContextEntry;
    }

    @Override
    public void setPartitionContextEntry(Entry partitionContextEntry) {
        this.partitionContextEntry = partitionContextEntry;
    }

    @Override
    public String getPartitionId() {
        return this.partitionId;
    }

    @Override
    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public Dn getPartitionSuffix() {
        return this.partitionSuffix;
    }

    public void setPartitionSuffix(Dn partitionSuffix) {
        this.partitionSuffix = partitionSuffix;
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
