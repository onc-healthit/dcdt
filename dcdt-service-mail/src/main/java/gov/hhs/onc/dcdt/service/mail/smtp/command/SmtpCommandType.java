package gov.hhs.onc.dcdt.service.mail.smtp.command;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum SmtpCommandType implements ToolIdentifier {
    AUTH, DATA, EHLO, HELO, MAIL, NOOP, QUIT, RCPT, RSET, STARTTLS, VRFY;

    @Override
    public String getId() {
        return this.name();
    }
}
