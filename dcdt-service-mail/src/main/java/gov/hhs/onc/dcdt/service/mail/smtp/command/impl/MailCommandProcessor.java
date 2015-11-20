package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.MailCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcMail")
public class MailCommandProcessor extends AbstractSmtpCommandProcessor<MailCommand> {
    public MailCommandProcessor() {
        super(SmtpCommandType.MAIL);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, MailCommand cmd)
        throws SmtpCommandException {
        if (!session.hasHeloName()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "HELO/EHLO command must be processed prior to MAIL"));
        }

        MailAddress fromAddr = cmd.getFrom();
        // noinspection ConstantConditions
        InstanceMailAddressConfig fromConfig = (gateway.hasAddressConfigs() ? gateway.getAddressConfigs().get(fromAddr) : null);

        if ((fromConfig != null) && !session.hasAuthenticatedAddressConfig()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.AUTH_REQUIRED, "Authentication required"));
        }

        session.setFrom(fromAddr);
        session.setFromConfig(fromConfig);
        session.setTo(null);
        session.setToConfig(null);

        return super.process(channel, gateway, config, session, cmd);
    }
}
