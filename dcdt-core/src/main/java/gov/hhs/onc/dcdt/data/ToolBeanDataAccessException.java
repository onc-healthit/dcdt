package gov.hhs.onc.dcdt.data;

import org.springframework.dao.DataAccessException;

public class ToolBeanDataAccessException extends DataAccessException {
    private final static long serialVersionUID = 0L;

    public ToolBeanDataAccessException() {
        super(null);
    }

    public ToolBeanDataAccessException(Throwable cause) {
        super(null, cause);
    }

    public ToolBeanDataAccessException(String msg) {
        super(msg);
    }

    public ToolBeanDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
