package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.channel.Channel;

public class RcptCommand extends AbstractSmtpCommand {
    public final static String TO_PREFIX = "TO:" + ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX;

    private MailAddress to;

    public RcptCommand(MailAddress to) {
        super(SmtpCommandType.RCPT);

        this.to = to;
    }

    public static RcptCommand parse(String str) throws SmtpCommandException {
        return new RcptCommand(parseMailAddress(TO_PREFIX, str));
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        SmtpServerSession serverSession = channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get();

        if (!serverSession.hasFrom()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "MAIL command must be processed prior to RCPT"));
        }

        serverSession.setTo(this.to);

        return super.process(channel);
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return ((ToolStrBuilder) builder.append(TO_PREFIX).append(this.to.toAddress(BindingType.ADDRESS))
            .append(ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX));
    }

    public MailAddress getTo() {
        return this.to;
    }
}
