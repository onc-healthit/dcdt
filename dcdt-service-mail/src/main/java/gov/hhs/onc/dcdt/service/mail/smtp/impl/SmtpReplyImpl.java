package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.service.mail.MailProtocolMessageDirection;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import org.apache.commons.lang3.StringUtils;

public class SmtpReplyImpl extends AbstractSmtpMessage implements SmtpReply {
    private SmtpReplyCode code;
    private String[] params;

    public SmtpReplyImpl(SmtpReplyCode code, String ... params) {
        this(MailProtocolMessageDirection.SERVER, code, params);
    }

    public SmtpReplyImpl(MailProtocolMessageDirection direction, SmtpReplyCode code, String ... params) {
        super(direction, code.getId());

        this.code = code;
        this.params = params;
    }

    @Override
    public String toString() {
        ToolStrBuilder strBuilder = new ToolStrBuilder();

        if (this.hasParameters()) {
            int lastParamIndex = (this.params.length - 1);
            String param;

            for (int a = 0; a < this.params.length; a++) {
                strBuilder.append(this.id);

                if (!(param = this.params[a]).isEmpty()) {
                    strBuilder.append(((a == lastParamIndex) ? StringUtils.SPACE : ToolStringUtils.HYPHEN));
                    strBuilder.append(param);
                }

                strBuilder.append(ToolStringUtils.CRLF);
            }
        } else {
            strBuilder.append(this.id);
            strBuilder.append(ToolStringUtils.CRLF);
        }

        return strBuilder.build();
    }

    @Override
    public SmtpReplyCode getCode() {
        return this.code;
    }

    @Override
    public boolean hasParameters() {
        return (this.params.length != 0);
    }

    @Override
    public String[] getParameters() {
        return this.params;
    }
}
