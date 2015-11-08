package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpAuthType;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.capability.impl.AuthCapability;
import gov.hhs.onc.dcdt.service.mail.smtp.capability.impl.SizeCapability;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import io.netty.channel.Channel;
import org.xbill.DNS.Name;

public class EhloCommand extends HeloCommand {
    public EhloCommand(Name heloName) {
        super(SmtpCommandType.EHLO, heloName);
    }

    public static EhloCommand parse(String str) throws SmtpCommandException {
        return new EhloCommand(parseHeloName(str));
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get().setHeloName(this.heloName);

        SmtpServerConfig config = ((SmtpServerConfig) channel.attr(AbstractToolChannelServer.CONFIG_ATTR_KEY).get());

        return new SmtpReplyImpl(SmtpReplyCode.MAIL_OK, config.getHeloName().toString(false), new AuthCapability(SmtpAuthType.LOGIN).toString(),
            new SizeCapability(config.getMaxDataFrameLength()).toString());
    }
}
