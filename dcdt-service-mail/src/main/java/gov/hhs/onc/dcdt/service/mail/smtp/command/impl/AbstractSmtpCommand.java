package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.service.mail.MailProtocolMessageDirection;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.AbstractSmtpMessage;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.channel.Channel;
import java.util.function.Function;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSmtpCommand extends AbstractSmtpMessage implements SmtpCommand {
    protected SmtpCommandType type;

    protected AbstractSmtpCommand(SmtpCommandType type) {
        super(MailProtocolMessageDirection.CLIENT, type.getId());

        this.type = type;
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        return new SmtpReplyImpl(SmtpReplyCode.MAIL_OK, SmtpReplyParameters.OK);
    }

    @Override
    public String toString() {
        ToolStrBuilder builder = this.parametersToString(new ToolStrBuilder());

        if (!builder.isEmpty()) {
            builder.insert(0, StringUtils.SPACE);
        }

        builder.insert(0, this.id);
        builder.append(ToolStringUtils.CRLF);

        return builder.build();
    }

    protected static MailAddress parseMailAddress(String prefix, String str) throws SmtpCommandException {
        String[] params = parseParameters(1, str);

        if (!StringUtils.startsWith(params[0], prefix) || !StringUtils.endsWith(params[0], ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX)) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Required syntax: '%slocal@domain%s'", prefix,
                ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX)));
        }

        if (!ToolMailAddressUtils.isAddress((params[0] =
            StringUtils.removeEnd(StringUtils.removeStart(params[0], prefix), ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX)))) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Malformed email address: %s", params[0])));
        }

        return new MailAddressImpl(params[0]);
    }

    protected static String[] parseParameters(@Nonnegative int numParams, String str) throws SmtpCommandException {
        return parseParameters(numParams, numParams, str);
    }

    protected static String[] parseParameters(@Nonnegative int minNumParams, @Nonnegative int maxNumParams, String str) throws SmtpCommandException {
        return parseParameters((strSplit) -> StringUtils.split(strSplit, StringUtils.SPACE), minNumParams, maxNumParams, str);
    }

    protected static String[] parseParameters(Function<String, String[]> splitter, @Nonnegative int numParams, String str) throws SmtpCommandException {
        return parseParameters(splitter, numParams, numParams, str);
    }

    protected static String[] parseParameters(Function<String, String[]> splitter, @Nonnegative int minNumParams, @Nonnegative int maxNumParams, String str)
        throws SmtpCommandException {
        String[] params = splitter.apply(str);

        if (params.length < minNumParams) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Incorrect number of parameters: %d < %d",
                params.length, minNumParams)));
        }

        if (params.length > maxNumParams) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Incorrect number of parameters: %d > %d",
                params.length, maxNumParams)));
        }

        return params;
    }

    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return builder;
    }

    @Override
    public SmtpCommandType getType() {
        return this.type;
    }
}
