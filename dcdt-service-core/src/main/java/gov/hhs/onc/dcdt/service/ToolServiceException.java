package gov.hhs.onc.dcdt.service;

import gov.hhs.onc.dcdt.ToolException;

public class ToolServiceException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolServiceException() {
        super();
    }

    public ToolServiceException(String msg) {
        super(msg);
    }

    public ToolServiceException(Throwable cause) {
        super(cause);
    }

    public ToolServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
