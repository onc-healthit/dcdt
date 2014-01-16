package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.ToolException;

public class LdifException extends ToolException {
    private final static long serialVersionUID = 0L;

    public LdifException() {
        super();
    }

    public LdifException(Throwable cause) {
        super(cause);
    }

    public LdifException(String message) {
        super(message);
    }

    public LdifException(String message, Throwable cause) {
        super(message, cause);
    }
}
