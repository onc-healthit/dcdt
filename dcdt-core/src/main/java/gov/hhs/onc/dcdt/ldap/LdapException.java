package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.ToolException;

public class LdapException extends ToolException {
    private final static long serialVersionUID = 0L;

    public LdapException() {
        super();
    }

    public LdapException(Throwable cause) {
        super(cause);
    }

    public LdapException(String message) {
        super(message);
    }

    public LdapException(String message, Throwable cause) {
        super(message, cause);
    }
}
