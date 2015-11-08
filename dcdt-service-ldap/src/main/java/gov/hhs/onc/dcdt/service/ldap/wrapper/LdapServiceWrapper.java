package gov.hhs.onc.dcdt.service.ldap.wrapper;

import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;
import gov.hhs.onc.dcdt.service.ldap.server.LdapServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public interface LdapServiceWrapper extends ToolServiceWrapper<LdapServerConfig, LdapServer, LdapService> {
}
