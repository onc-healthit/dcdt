package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpAuthMechanism;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.capability.impl.AuthCapability;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.HeloCommand;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcEhlo")
public class EhloCommandProcessor extends HeloCommandProcessor {
    public EhloCommandProcessor() {
        super(SmtpCommandType.EHLO);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, HeloCommand cmd)
        throws SmtpCommandException {
        SmtpReply reply = super.process(channel, gateway, config, session, cmd);
        reply.setParameters(ArrayUtils.addAll(reply.getParameters(), new AuthCapability(SmtpAuthMechanism.LOGIN).toString()));

        return reply;
    }
}
