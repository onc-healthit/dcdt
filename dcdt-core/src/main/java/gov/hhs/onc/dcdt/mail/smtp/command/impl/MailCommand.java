package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;

public class MailCommand extends AbstractSmtpCommand {
    public final static String FROM_PREFIX = "FROM:" + ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX;

    private MailAddress fromAddr;

    public MailCommand(MailAddress fromAddr) {
        super(SmtpCommandType.MAIL);

        this.fromAddr = fromAddr;
    }

    public static MailCommand parse(String str) throws SmtpCommandException {
        return new MailCommand(parsePath(-1, FROM_PREFIX, str));
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return ((ToolStrBuilder) builder.append(FROM_PREFIX).append(this.fromAddr.toAddress(BindingType.ADDRESS))
            .append(ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX));
    }

    public MailAddress getFrom() {
        return this.fromAddr;
    }
}
