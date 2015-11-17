package gov.hhs.onc.dcdt.service.mail.smtp.command;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;

public interface SmtpCommandProcessor<T extends SmtpCommand> {
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, T cmd) throws SmtpCommandException;

    public SmtpCommandType getType();
}
