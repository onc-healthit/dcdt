package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.DataCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@Component("smtpCmdProcData")
public class DataCommandProcessor extends AbstractSmtpCommandProcessor<DataCommand> {
    public DataCommandProcessor() {
        super(SmtpCommandType.DATA);
    }

    @Override
    public SmtpReply process(Channel channel, MailGateway gateway, SmtpServerConfig config, SmtpServerSession session, DataCommand cmd)
        throws SmtpCommandException {
        if (!session.hasTo()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "RCPT command must be processed prior to DATA"));
        }

        return new SmtpReplyImpl(SmtpReplyCode.DATA_READY, "End data with <CR><LF>.<CR><LF>");
    }
}
