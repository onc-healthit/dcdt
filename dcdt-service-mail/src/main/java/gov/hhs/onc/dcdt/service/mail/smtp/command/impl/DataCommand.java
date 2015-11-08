package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import io.netty.channel.Channel;

public class DataCommand extends AbstractSmtpCommand {
    public DataCommand() {
        super(SmtpCommandType.DATA);
    }

    public static DataCommand parse(String str) throws SmtpCommandException {
        return new DataCommand();
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        if (!channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get().hasTo()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "RCPT command must be processed prior to DATA"));
        }

        return new SmtpReplyImpl(SmtpReplyCode.DATA_READY, "End data with <CR><LF>.<CR><LF>");
    }
}
