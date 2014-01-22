package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.ldap.LdapBindConfig;
import gov.hhs.onc.dcdt.ldap.LdapSslType;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.Name;

public interface InstanceLdapConfig extends ToolNamedBean {
    public LdapConnectionConfig toConnectionConfigAdmin();

    public LdapConnectionConfig toConnectionConfigAnonymous();

    public LdapBindConfig getBindConfigAdmin();

    public void setBindConfigAdmin(LdapBindConfig bindConfigAdmin);

    public LdapBindConfig getBindConfigAnonymous();

    public void setBindConfigAnonymous(LdapBindConfig bindConfigAnon);

    public Name getHost();

    public void setHost(Name host);

    public int getPort();

    public void setPort(int port);

    public LdapSslType getSslType();

    public void setSslType(LdapSslType sslType);
}
