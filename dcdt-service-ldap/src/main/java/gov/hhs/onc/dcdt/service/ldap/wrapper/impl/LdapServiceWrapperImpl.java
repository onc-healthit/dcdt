package gov.hhs.onc.dcdt.service.ldap.wrapper.impl;

import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;
import gov.hhs.onc.dcdt.service.ldap.impl.LdapServiceImpl;
import gov.hhs.onc.dcdt.service.ldap.server.LdapServer;
import gov.hhs.onc.dcdt.service.ldap.wrapper.LdapServiceWrapper;
import gov.hhs.onc.dcdt.service.wrapper.impl.AbstractToolServiceWrapper;

public class LdapServiceWrapperImpl extends AbstractToolServiceWrapper<LdapServerConfig, LdapServer, LdapService> implements LdapServiceWrapper {
    public LdapServiceWrapperImpl(String ... args) {
        super(LdapService.class, LdapServiceImpl.class, args);
    }

    public static void main(String ... args) {
        new LdapServiceWrapperImpl(args).start();
    }
}
