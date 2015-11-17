package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import org.xbill.DNS.Name;

public class EhloCommand extends HeloCommand {
    public EhloCommand(Name heloName) {
        super(SmtpCommandType.EHLO, heloName);
    }

    public static EhloCommand parse(String str) throws SmtpCommandException {
        return new EhloCommand(parseHeloName(str));
    }
}
