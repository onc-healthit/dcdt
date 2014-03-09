package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBoundBean;
import gov.hhs.onc.dcdt.config.impl.InstanceLdapConfigImpl;
import gov.hhs.onc.dcdt.ldap.LdapBindCredentialConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@JsonSubTypes({ @Type(InstanceLdapConfigImpl.class) })
public interface InstanceLdapConfig extends ToolBoundBean {
    public LdapConnectionConfig toConnectionConfigAdmin();

    public LdapConnectionConfig toConnectionConfigAnonymous();

    public LdapBindCredentialConfig getBindCredentialConfigAdmin();

    public void setBindCredentialConfigAdmin(LdapBindCredentialConfig bindCredConfigAdmin);

    public LdapBindCredentialConfig getBindCredentialConfigAnonymous();

    public void setBindCredentialConfigAnonymous(LdapBindCredentialConfig bindCredConfigAnon);

    public Entry getPartitionContextEntry();

    public void setPartitionContextEntry(Entry partitionContextEntry);

    public String getPartitionId();

    public void setPartitionId(String partitionId);

    public Dn getPartitionSuffix();

    public void setPartitionSuffix(Dn partitionSuffix);

    public String getServerId();

    public void setServerId(String serverId);

    @JsonProperty("sslType")
    public LdapSslType getSslType();

    public void setSslType(LdapSslType sslType);
}
