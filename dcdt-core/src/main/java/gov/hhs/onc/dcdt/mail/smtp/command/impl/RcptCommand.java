package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;

public class RcptCommand extends AbstractSmtpCommand {
    public final static String TO_PREFIX = "TO:" + ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_PREFIX;

    private MailAddress toAddr;

    public RcptCommand(MailAddress toAddr) {
        super(SmtpCommandType.RCPT);

        this.toAddr = toAddr;
    }

    public static RcptCommand parse(String str) throws SmtpCommandException {
        return new RcptCommand(parsePath(-1, TO_PREFIX, str));
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return ((ToolStrBuilder) builder.append(TO_PREFIX).append(this.toAddr.toAddress(BindingType.ADDRESS))
            .append(ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX));
    }

    public MailAddress getTo() {
        return this.toAddr;
    }
}
