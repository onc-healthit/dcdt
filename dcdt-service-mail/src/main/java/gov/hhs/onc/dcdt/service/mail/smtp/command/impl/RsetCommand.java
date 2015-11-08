package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import io.netty.channel.Channel;

public class RsetCommand extends AbstractSmtpCommand {
    public RsetCommand() {
        super(SmtpCommandType.RSET);
    }

    public static RsetCommand parse(String str) throws SmtpCommandException {
        return new RsetCommand();
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get().reset();

        return super.process(channel);
    }
}
