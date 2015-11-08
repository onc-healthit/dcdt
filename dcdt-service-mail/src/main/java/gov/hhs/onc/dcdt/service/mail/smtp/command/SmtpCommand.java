package gov.hhs.onc.dcdt.service.mail.smtp.command;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpMessage;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import io.netty.channel.Channel;

public interface SmtpCommand extends SmtpMessage {
    public SmtpReply process(Channel channel) throws SmtpCommandException;

    public SmtpCommandType getType();
}
