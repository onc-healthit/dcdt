package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;

public class ToolLdapLookupException extends ToolLdapException {
    private final static long serialVersionUID = 0L;

    public ToolLdapLookupException() {
        super();
    }

    public ToolLdapLookupException(Throwable cause) {
        super(cause);
    }

    public ToolLdapLookupException(String message) {
        super(message);
    }

    public ToolLdapLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
