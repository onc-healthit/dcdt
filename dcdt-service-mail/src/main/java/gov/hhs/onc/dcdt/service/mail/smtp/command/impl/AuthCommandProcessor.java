package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpAuthMechanism;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.AuthCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcAuth")
public class AuthCommandProcessor extends AbstractSmtpCommandProcessor<AuthCommand> {
    public AuthCommandProcessor() {
        super(SmtpCommandType.AUTH);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, AuthCommand cmd)
        throws SmtpCommandException {
        if (session.hasAuthenticatedAddressConfig()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "Already authenticated."));
        }

        session.resetAuthentication();
        session.setAuthenticationSecret(cmd.getSecret());

        SmtpAuthMechanism authType = cmd.getAuthType();

        if (authType != SmtpAuthMechanism.LOGIN) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.PARAMETER_UNIMPLEMENTED, String.format("Unavailable authentication mechanism: %s",
                authType.getId())));
        }

        return new SmtpReplyImpl(SmtpReplyCode.AUTH_READY, SmtpReplyParameters.AUTH_ID_PROMPT);
    }
}
