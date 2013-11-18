package gov.hhs.onc.dcdt.beans;


import org.springframework.beans.BeansException;

public class ToolBeanException extends BeansException {
    private final static long serialVersionUID = 0L;

    public ToolBeanException() {
        super(null);
    }

    public ToolBeanException(String msg) {
        super(msg);
    }

    public ToolBeanException(Throwable cause) {
        super(null, cause);
    }

    public ToolBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
