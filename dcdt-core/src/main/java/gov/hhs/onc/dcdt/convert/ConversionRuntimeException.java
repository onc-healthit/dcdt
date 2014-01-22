package gov.hhs.onc.dcdt.convert;

import gov.hhs.onc.dcdt.ToolRuntimeException;

public class ConversionRuntimeException extends ToolRuntimeException {
    private final static long serialVersionUID = 0L;

    public ConversionRuntimeException() {
        super();
    }

    public ConversionRuntimeException(String msg) {
        super(msg);
    }

    public ConversionRuntimeException(Throwable cause) {
        super(cause);
    }

    public ConversionRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
