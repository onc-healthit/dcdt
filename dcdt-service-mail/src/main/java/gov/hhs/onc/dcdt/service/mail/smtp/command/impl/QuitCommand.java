package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import io.netty.channel.Channel;

public class QuitCommand extends AbstractSmtpCommand {
    public QuitCommand() {
        super(SmtpCommandType.QUIT);
    }

    public static QuitCommand parse(String str) throws SmtpCommandException {
        return new QuitCommand();
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        return new SmtpReplyImpl(SmtpReplyCode.SYSTEM_QUIT, String.format("%s closing connection",
            ((SmtpServerConfig) channel.attr(AbstractToolChannelServer.CONFIG_ATTR_KEY).get()).getHeloName().toString(true)));
    }
}
