package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.channel.Channel;

public class MailCommand extends AbstractSmtpCommand {
    public final static String FROM_PREFIX = "FROM:" + ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX;

    private MailAddress from;

    public MailCommand(MailAddress from) {
        super(SmtpCommandType.MAIL);

        this.from = from;
    }

    public static MailCommand parse(String str) throws SmtpCommandException {
        return new MailCommand(parseMailAddress(FROM_PREFIX, str));
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get().setFrom(this.from);

        return super.process(channel);
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return ((ToolStrBuilder) builder.append(FROM_PREFIX).append(this.from.toAddress(BindingType.ADDRESS))
            .append(ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX));
    }

    public MailAddress getFrom() {
        return this.from;
    }
}
