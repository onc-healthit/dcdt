package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.QuitCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcQuit")
public class QuitCommandProcessor extends AbstractSmtpCommandProcessor<QuitCommand> {
    public QuitCommandProcessor() {
        super(SmtpCommandType.QUIT);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, QuitCommand cmd)
        throws SmtpCommandException {
        return new SmtpReplyImpl(SmtpReplyCode.SYSTEM_QUIT, String.format("%s closing connection", config.getHeloName().toString(true)));
    }
}
