package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Name;

public class HeloCommand extends AbstractSmtpCommand {
    protected Name heloName;

    public HeloCommand(Name heloName) {
        this(SmtpCommandType.HELO, heloName);
    }

    protected HeloCommand(SmtpCommandType type, Name heloName) {
        super(type);

        this.heloName = heloName;
    }

    public static HeloCommand parse(String str) throws SmtpCommandException {
        return new HeloCommand(parseHeloName(str));
    }

    protected static Name parseHeloName(String str) throws SmtpCommandException {
        String[] params = parseParameters(1, str);

        if (StringUtils.isBlank(params[0])) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Malformed HELO name: %s", params[0])));
        }

        try {
            return ToolDnsNameUtils.fromString(params[0]);
        } catch (DnsNameException e) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Malformed HELO name: %s", params[0])), e);
        }
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return ((ToolStrBuilder) builder.append(this.heloName.toString(true)));
    }

    public Name getHeloName() {
        return this.heloName;
    }
}
