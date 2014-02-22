package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.ldap.LdapException;

public class LdapLookupException extends LdapException {
    private final static long serialVersionUID = 0L;

    public LdapLookupException() {
        super();
    }

    public LdapLookupException(Throwable cause) {
        super(cause);
    }

    public LdapLookupException(String message) {
        super(message);
    }

    public LdapLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
