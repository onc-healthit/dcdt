package gov.hhs.onc.dcdt.ldap;

import org.apache.directory.api.ldap.model.exception.LdapException;

public class ToolLdapException extends LdapException {
    private final static long serialVersionUID = 0L;

    public ToolLdapException() {
        super();
    }

    public ToolLdapException(Throwable cause) {
        super(cause);
    }

    public ToolLdapException(String message) {
        super(message);
    }

    public ToolLdapException(String message, Throwable cause) {
        super(message, cause);
    }
}
