package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.ToolServiceException;

public class MailServiceException extends ToolServiceException {
    private final static long serialVersionUID = 0L;

    public MailServiceException() {
        super();
    }

    public MailServiceException(String msg) {
        super(msg);
    }

    public MailServiceException(Throwable cause) {
        super(cause);
    }

    public MailServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
