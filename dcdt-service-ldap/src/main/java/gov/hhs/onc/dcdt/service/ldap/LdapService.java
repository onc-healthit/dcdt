package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;
import gov.hhs.onc.dcdt.service.ldap.server.LdapServer;

public interface LdapService extends ToolService<LdapServerConfig, LdapServer> {
}
