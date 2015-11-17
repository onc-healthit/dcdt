package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;

public class RsetCommand extends AbstractSmtpCommand {
    public RsetCommand() {
        super(SmtpCommandType.RSET);
    }

    public static RsetCommand parse(String str) throws SmtpCommandException {
        parseParameters(0, str);

        return new RsetCommand();
    }
}
