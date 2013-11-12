package gov.hhs.onc.dcdt.service.ldap.wrapper;


import gov.hhs.onc.dcdt.service.ldap.ToolLdapService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public class ToolLdapServiceWrapper extends ToolServiceWrapper<ToolLdapService> {
    public ToolLdapServiceWrapper() {
        super();
    }

    public ToolLdapServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolLdapServiceWrapper(args).start();
    }

    @Override
    protected ToolLdapService createService() {
        return new ToolLdapService();
    }
}
