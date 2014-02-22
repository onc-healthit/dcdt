package gov.hhs.onc.dcdt.service.ldap.factory.impl;

import org.apache.directory.server.factory.DefaultLdapServerFactory;
import org.apache.directory.server.ldap.LdapServer;

public class ToolLdapServerFactory extends DefaultLdapServerFactory {
    public ToolLdapServerFactory() throws Exception {
        super();
    }

    @Override
    public LdapServer getLdapServer() {
        return new LdapServer();
    }
}
