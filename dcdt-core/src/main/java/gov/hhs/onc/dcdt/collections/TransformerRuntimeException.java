package gov.hhs.onc.dcdt.collections;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class TransformerRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public TransformerRuntimeException() {
        super();
    }

    public TransformerRuntimeException(String msg) {
        super(msg);
    }

    public TransformerRuntimeException(Throwable cause) {
        super(cause);
    }

    public TransformerRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
