package gov.hhs.onc.dcdt.data.registry;

import gov.hhs.onc.dcdt.beans.ToolBeanException;

public class ToolBeanRegistryException extends ToolBeanException {
    private final static long serialVersionUID = 0L;

    public ToolBeanRegistryException() {
        super();
    }

    public ToolBeanRegistryException(String msg) {
        super(msg);
    }

    public ToolBeanRegistryException(Throwable cause) {
        super(cause);
    }

    public ToolBeanRegistryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
