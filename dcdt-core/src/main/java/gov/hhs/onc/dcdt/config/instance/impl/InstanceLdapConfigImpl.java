package gov.hhs.onc.dcdt.config.instance.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapCredentialConfig;
import gov.hhs.onc.dcdt.ldap.LdapTransportProtocol;
import gov.hhs.onc.dcdt.net.SslType;
import java.util.Objects;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@JsonTypeName("instanceLdapConfig")
public class InstanceLdapConfigImpl extends AbstractToolConnectionBean<LdapTransportProtocol> implements InstanceLdapConfig {
    private InstanceLdapCredentialConfig credConfigAdmin;
    private InstanceLdapCredentialConfig credConfigAnon;
    private Entry dataPartitionContextEntry;
    private String dataPartitionId;
    private Dn dataPartitionSuffix;
    private String serverId;

    @Override
    public LdapConnectionConfig toConnectionConfigAdmin() {
        return this.toConnectionConfig(this.credConfigAdmin);
    }

    @Override
    public LdapConnectionConfig toConnectionConfigAnonymous() {
        return this.toConnectionConfig(this.credConfigAnon);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.dataPartitionContextEntry == null) {
            this.dataPartitionContextEntry = new DefaultEntry(this.dataPartitionSuffix);
            this.dataPartitionContextEntry.add(SchemaConstants.OBJECT_CLASS_AT, SchemaConstants.ORGANIZATIONAL_UNIT_OC, SchemaConstants.TOP_OC);
            this.dataPartitionContextEntry.add(this.dataPartitionSuffix.getRdn().getType(), this.dataPartitionSuffix.getRdn().getValue());
        }
    }

    private LdapConnectionConfig toConnectionConfig(InstanceLdapCredentialConfig credConfig) {
        SslType sslType = this.transportProtocol.getSslType();

        LdapConnectionConfig ldapConnConfig = new LdapConnectionConfig();
        // noinspection ConstantConditions
        ldapConnConfig.setLdapHost(this.getHost(true).getHostAddress());
        ldapConnConfig.setLdapPort(this.port);
        ldapConnConfig.setUseSsl((sslType == SslType.SSL));
        ldapConnConfig.setUseTls((sslType == SslType.STARTTLS));
        ldapConnConfig.setName(Objects.toString(credConfig.getId(), null));
        ldapConnConfig.setCredentials(credConfig.getSecret());

        return ldapConnConfig;
    }

    @Override
    public InstanceLdapCredentialConfig getCredentialConfigAdmin() {
        return this.credConfigAdmin;
    }

    @Override
    public void setCredentialConfigAdmin(InstanceLdapCredentialConfig credConfigAdmin) {
        this.credConfigAdmin = credConfigAdmin;
    }

    @Override
    public InstanceLdapCredentialConfig getCredentialConfigAnonymous() {
        return this.credConfigAnon;
    }

    @Override
    public void setCredentialConfigAnonymous(InstanceLdapCredentialConfig credConfigAnon) {
        this.credConfigAnon = credConfigAnon;
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
}
