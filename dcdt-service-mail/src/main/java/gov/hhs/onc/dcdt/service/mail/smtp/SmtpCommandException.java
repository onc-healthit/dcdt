package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.service.mail.MailServiceException;
import javax.annotation.Nullable;

public class SmtpCommandException extends MailServiceException {
    private final static long serialVersionUID = 0L;

    private SmtpReply resp;

    public SmtpCommandException(SmtpReply resp) {
        this(resp, null);
    }

    public SmtpCommandException(SmtpReply resp, @Nullable Throwable cause) {
        super(null, cause);

        this.resp = resp;
    }

    public SmtpReply getResponse() {
        return this.resp;
    }
}
