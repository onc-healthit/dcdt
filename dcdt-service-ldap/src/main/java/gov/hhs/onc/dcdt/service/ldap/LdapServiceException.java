package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.service.ToolServiceException;

public class LdapServiceException extends ToolServiceException {
    private final static long serialVersionUID = 0L;

    public LdapServiceException() {
        super();
    }

    public LdapServiceException(String msg) {
        super(msg);
    }

    public LdapServiceException(Throwable cause) {
        super(cause);
    }

    public LdapServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
