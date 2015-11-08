package gov.hhs.onc.dcdt.service.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpAuthType;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.smtp.impl.SmtpServerImpl;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import javax.annotation.Nullable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class AuthCommand extends AbstractSmtpCommand {
    private SmtpAuthType authType;
    private String secret;

    public AuthCommand(SmtpAuthType authType) {
        this(authType, null);
    }

    public AuthCommand(SmtpAuthType authType, @Nullable String secret) {
        super(SmtpCommandType.AUTH);

        this.authType = authType;
        this.secret = secret;
    }

    public static AuthCommand parse(String str) throws SmtpCommandException {
        String[] params = parseParameters(1, 2, str);

        SmtpAuthType authType = ToolEnumUtils.findById(SmtpAuthType.class, params[0]);

        if (authType == null) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Unknown authentication type: %s", params[0])));
        }

        String secret = null;

        if (params.length == 2) {
            secret = params[1];

            if (StringUtils.isBlank(secret)) {
                throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Malformed authentication value: %s",
                    secret)));
            }

            secret = new String(Base64.decodeBase64(secret), CharsetUtil.US_ASCII);
        }

        return new AuthCommand(authType, secret);
    }

    @Override
    public SmtpReply process(Channel channel) throws SmtpCommandException {
        SmtpServerSession serverSession = channel.attr(SmtpServerImpl.SERVER_SESSION_ATTR_KEY).get();

        if (serverSession.hasAuthenticatedConfig()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.BAD_SEQUENCE, "Already authenticated."));
        }

        serverSession.resetAuthentication();
        serverSession.setAuthenticationSecret(this.secret);

        if (this.authType != SmtpAuthType.LOGIN) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.PARAMETER_UNIMPLEMENTED, String.format("Unavailable authentication mechanism: %s",
                this.authType.getId())));
        }

        return new SmtpReplyImpl(SmtpReplyCode.AUTH_READY, SmtpReplyParameters.AUTH_ID_PROMPT);
    }

    @Override
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return builder.appendWithDelimiters(ArrayUtils.toArray(this.authType.getId(), this.secret), StringUtils.SPACE);
    }

    public SmtpAuthType getAuthType() {
        return this.authType;
    }

    public boolean hasSecret() {
        return (this.secret != null);
    }

    @Nullable
    public String getSecret() {
        return this.secret;
    }
}
