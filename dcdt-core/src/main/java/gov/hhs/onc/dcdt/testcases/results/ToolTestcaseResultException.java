package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.ToolException;

public class ToolTestcaseResultException extends ToolException {
    private final static long serialVersionUID = 0L;

    public ToolTestcaseResultException() {
        super();
    }

    public ToolTestcaseResultException(Throwable cause) {
        super(cause);
    }

    public ToolTestcaseResultException(String message) {
        super(message);
    }

    public ToolTestcaseResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
