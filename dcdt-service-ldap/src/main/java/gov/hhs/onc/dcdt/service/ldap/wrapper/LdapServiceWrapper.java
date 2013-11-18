package gov.hhs.onc.dcdt.service.ldap.wrapper;


import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public class LdapServiceWrapper extends ToolServiceWrapper<LdapService> {
    public LdapServiceWrapper() {
        super();
    }

    public LdapServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new LdapServiceWrapper(args).start();
    }

    @Override
    protected LdapService createService() {
        return new LdapService();
    }
}
