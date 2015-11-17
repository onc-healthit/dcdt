package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;

public class NoopCommand extends AbstractSmtpCommand {
    public NoopCommand() {
        super(SmtpCommandType.NOOP);
    }

    public static NoopCommand parse(String str) throws SmtpCommandException {
        parseParameters(0, str);

        return new NoopCommand();
    }
}
