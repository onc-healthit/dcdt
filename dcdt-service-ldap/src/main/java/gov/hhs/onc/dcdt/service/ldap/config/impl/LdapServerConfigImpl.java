package gov.hhs.onc.dcdt.service.ldap.config.impl;

import gov.hhs.onc.dcdt.ldap.LdapTransportProtocol;
import gov.hhs.onc.dcdt.service.config.impl.AbstractToolServerConfig;
import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;

public class LdapServerConfigImpl extends AbstractToolServerConfig<LdapTransportProtocol> implements LdapServerConfig {
    public LdapServerConfigImpl() {
        super("LDAP");
    }
}
