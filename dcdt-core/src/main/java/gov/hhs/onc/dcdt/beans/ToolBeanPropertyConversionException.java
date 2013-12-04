package gov.hhs.onc.dcdt.beans;


import org.springframework.core.convert.ConversionException;

public class ToolBeanPropertyConversionException extends ConversionException {
    private final static long serialVersionUID = 0L;

    public ToolBeanPropertyConversionException() {
        super(null);
    }

    public ToolBeanPropertyConversionException(String msg) {
        super(msg);
    }

    public ToolBeanPropertyConversionException(Throwable cause) {
        super(null, cause);
    }

    public ToolBeanPropertyConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
