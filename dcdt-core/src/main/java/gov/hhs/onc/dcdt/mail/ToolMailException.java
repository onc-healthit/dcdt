package gov.hhs.onc.dcdt.mail;

import javax.mail.MessagingException;

public class ToolMailException extends MessagingException {
    private final static long serialVersionUID = 0L;

    public ToolMailException() {
        this(null, null);
    }

    public ToolMailException(String msg) {
        this(msg, null);
    }

    public ToolMailException(Throwable cause) {
        this(null, cause);
    }

    public ToolMailException(String msg, Throwable cause) {
        super(msg, ((Exception) cause));
    }
}
