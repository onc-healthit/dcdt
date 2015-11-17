package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandProcessor;
import io.netty.channel.Channel;

public abstract class AbstractSmtpCommandProcessor<T extends SmtpCommand> implements SmtpCommandProcessor<T> {
    protected SmtpCommandType type;

    protected AbstractSmtpCommandProcessor(SmtpCommandType type) {
        this.type = type;
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, T cmd) throws SmtpCommandException {
        return new SmtpReplyImpl();
    }

    @Override
    public SmtpCommandType getType() {
        return this.type;
    }
}
