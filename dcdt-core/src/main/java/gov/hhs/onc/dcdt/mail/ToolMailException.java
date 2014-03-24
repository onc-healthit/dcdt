package gov.hhs.onc.dcdt.mail;

import org.springframework.mail.MailException;

public class ToolMailException extends MailException {
    private final static long serialVersionUID = 0L;

    public ToolMailException() {
        super(null, null);
    }

    public ToolMailException(String msg) {
        super(msg);
    }

    public ToolMailException(Throwable cause) {
        super(null, cause);
    }

    public ToolMailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
