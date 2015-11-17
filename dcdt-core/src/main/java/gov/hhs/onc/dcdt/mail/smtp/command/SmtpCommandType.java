package gov.hhs.onc.dcdt.mail.smtp.command;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum SmtpCommandType implements ToolIdentifier {
    AUTH, DATA, EHLO, EXPN, HELO, HELP, MAIL, NOOP, QUIT, RCPT, RSET, STARTTLS, VRFY;

    @Override
    public String getId() {
        return this.name();
    }
}
