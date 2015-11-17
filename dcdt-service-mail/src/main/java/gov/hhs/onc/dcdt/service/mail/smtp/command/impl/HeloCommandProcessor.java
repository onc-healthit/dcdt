package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.HeloCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("smtpCmdProcHelo")
public class HeloCommandProcessor extends AbstractSmtpCommandProcessor<HeloCommand> {
    public HeloCommandProcessor() {
        this(SmtpCommandType.HELO);
    }

    protected HeloCommandProcessor(SmtpCommandType type) {
        super(type);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, HeloCommand cmd)
        throws SmtpCommandException {
        Name heloName = cmd.getHeloName();
        session.reset();
        session.setHeloName(heloName);

        return new SmtpReplyImpl(String.format("%s Hello %s", config.getHeloName().toString(false), heloName.toString(false)));
    }
}
