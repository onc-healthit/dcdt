package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.RsetCommand;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcRset")
public class RsetCommandProcessor extends AbstractSmtpCommandProcessor<RsetCommand> {
    public RsetCommandProcessor() {
        super(SmtpCommandType.RSET);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, RsetCommand cmd)
        throws SmtpCommandException {
        session.reset();

        return super.process(channel, gateway, config, session, cmd);
    }
}
