package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.channel.Channel;
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

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get().setHeloName(this.heloName);

        return new SmtpReplyImpl(SmtpReplyCode.MAIL_OK, String.format("%s Hello %s", ((SmtpServerConfig) channel
            .attr(AbstractToolChannelServer.CONFIG_ATTR_KEY).get()).getHeloName().toString(false), this.heloName.toString(false)));
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
