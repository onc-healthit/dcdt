package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.RcptCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcRcpt")
public class RcptCommandProcessor extends AbstractSmtpCommandProcessor<RcptCommand> {
    public RcptCommandProcessor() {
        super(SmtpCommandType.RCPT);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, RcptCommand cmd)
        throws SmtpCommandException {
        if (!session.hasHeloName()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "HELO/EHLO command must be processed prior to RCPT"));
        }

        if (!session.hasFrom()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "MAIL command must be processed prior to RCPT"));
        }

        if (session.hasTo()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYSTEM_STORAGE_ERROR, "Only one recipient address is allowed"));
        }

        MailAddress toAddr = cmd.getTo();
        // noinspection ConstantConditions
        InstanceMailAddressConfig toConfig = (gateway.hasAddressConfigs() ? gateway.getAddressConfigs().get(toAddr) : null);

        if ((toConfig == null) && (!session.hasAuthenticatedAddressConfig() || !session.hasFromConfig())) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.MAILBOX_PERM_UNAVAILABLE, "Mailbox unavailable"));
        }

        session.setTo(toAddr);
        session.setToConfig(toConfig);
        session.setMailInfo(null);

        return super.process(channel, gateway, config, session, cmd);
    }
}
