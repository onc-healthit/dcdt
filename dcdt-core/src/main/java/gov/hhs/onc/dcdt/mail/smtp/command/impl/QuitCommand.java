package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;

public class QuitCommand extends AbstractSmtpCommand {
    public QuitCommand() {
        super(SmtpCommandType.QUIT);
    }

    public static QuitCommand parse(String str) throws SmtpCommandException {
        parseParameters(0, 0, str);

        return new QuitCommand();
    }
}
