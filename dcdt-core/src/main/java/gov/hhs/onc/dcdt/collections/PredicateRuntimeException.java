package gov.hhs.onc.dcdt.collections;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class PredicateRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public PredicateRuntimeException() {
        super();
    }

    public PredicateRuntimeException(String msg) {
        super(msg);
    }

    public PredicateRuntimeException(Throwable cause) {
        super(cause);
    }

    public PredicateRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
