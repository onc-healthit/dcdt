package gov.hhs.onc.dcdt.mail.crypto;

import gov.hhs.onc.dcdt.mail.ToolMailException;

public class ToolSmimeException extends ToolMailException {
    private final static long serialVersionUID = 0L;

    public ToolSmimeException() {
        super();
    }

    public ToolSmimeException(Throwable cause) {
        super(cause);
    }

    public ToolSmimeException(String message) {
        super(message);
    }

    public ToolSmimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
