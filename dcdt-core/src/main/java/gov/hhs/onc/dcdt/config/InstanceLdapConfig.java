package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.config.impl.InstanceLdapConfigImpl;
import gov.hhs.onc.dcdt.ldap.LdapBindConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.Name;

@JsonSubTypes({ @Type(InstanceLdapConfigImpl.class) })
public interface InstanceLdapConfig extends ToolNamedBean {
    public LdapConnectionConfig toConnectionConfigAdmin();

    public LdapConnectionConfig toConnectionConfigAnonymous();

    public LdapBindConfig getBindConfigAdmin();

    public void setBindConfigAdmin(LdapBindConfig bindConfigAdmin);

    public LdapBindConfig getBindConfigAnonymous();

    public void setBindConfigAnonymous(LdapBindConfig bindConfigAnon);

    @JsonProperty("host")
    public Name getHost();

    public void setHost(Name host);

    @JsonProperty("port")
    public int getPort();

    public void setPort(int port);

    @JsonProperty("sslType")
    public LdapSslType getSslType();

    public void setSslType(LdapSslType sslType);
}
