package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;

public class DataCommand extends AbstractSmtpCommand {
    public DataCommand() {
        super(SmtpCommandType.DATA);
    }

    public static DataCommand parse(String str) throws SmtpCommandException {
        parseParameters(0, str);

        return new DataCommand();
    }
}
