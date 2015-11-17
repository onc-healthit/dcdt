package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpAuthMechanism;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import io.netty.util.CharsetUtil;
import javax.annotation.Nullable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class AuthCommand extends AbstractSmtpCommand {
    private SmtpAuthMechanism authType;
    private String secret;

    public AuthCommand(SmtpAuthMechanism authType) {
        this(authType, null);
    }

    public AuthCommand(SmtpAuthMechanism authType, @Nullable String secret) {
        super(SmtpCommandType.AUTH);

        this.authType = authType;
        this.secret = secret;
    }

    public static AuthCommand parse(String str) throws SmtpCommandException {
        String[] params = parseParameters(1, 2, str);

        SmtpAuthMechanism authType = ToolEnumUtils.findById(SmtpAuthMechanism.class, params[0]);

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
    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return builder.appendWithDelimiters(ArrayUtils.toArray(this.authType.getId(), this.secret), StringUtils.SPACE);
    }

    public SmtpAuthMechanism getAuthType() {
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
