package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import io.netty.channel.Channel;

public class StartTlsCommand extends AbstractSmtpCommand {
    public StartTlsCommand() {
        super(SmtpCommandType.STARTTLS);
    }

    public static StartTlsCommand parse(String str) throws SmtpCommandException {
        return new StartTlsCommand();
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        return new SmtpReplyImpl(SmtpReplyCode.COMMAND_UNIMPLEMENTED, "Command is disabled");
    }
}
