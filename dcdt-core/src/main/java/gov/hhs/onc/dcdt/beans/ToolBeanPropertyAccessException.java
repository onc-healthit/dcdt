package gov.hhs.onc.dcdt.beans;


import java.beans.PropertyChangeEvent;
import org.springframework.beans.PropertyAccessException;

public class ToolBeanPropertyAccessException extends PropertyAccessException {
    private final static long serialVersionUID = 0L;

    private final static String ERR_CODE = "propAccess";

    public ToolBeanPropertyAccessException() {
        this(null, null, null);
    }

    public ToolBeanPropertyAccessException(String msg) {
        this(msg, null);
    }

    public ToolBeanPropertyAccessException(String msg, Throwable cause) {
        this(null, msg, cause);
    }

    public ToolBeanPropertyAccessException(PropertyChangeEvent propChangeEvent) {
        this(propChangeEvent, null, null);
    }

    public ToolBeanPropertyAccessException(PropertyChangeEvent propChangeEvent, Throwable cause) {
        this(propChangeEvent, null, cause);
    }

    public ToolBeanPropertyAccessException(PropertyChangeEvent propChangeEvent, String msg) {
        this(propChangeEvent, msg, null);
    }

    public ToolBeanPropertyAccessException(PropertyChangeEvent propChangeEvent, String msg, Throwable cause) {
        super(propChangeEvent, msg, cause);
    }

    @Override
    public String getErrorCode() {
        return ERR_CODE;
    }
}
