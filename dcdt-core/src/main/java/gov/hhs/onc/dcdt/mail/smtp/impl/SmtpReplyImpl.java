package gov.hhs.onc.dcdt.mail.smtp.impl;

import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import org.apache.commons.lang3.StringUtils;

public class SmtpReplyImpl extends AbstractSmtpMessage<SmtpReplyCode> implements SmtpReply {
    private String[] params;

    public SmtpReplyImpl() {
        this(SmtpReplyParameters.OK);
    }

    public SmtpReplyImpl(String ... params) {
        this(SmtpReplyCode.MAIL_OK, params);
    }

    public SmtpReplyImpl(SmtpReplyCode id, String ... params) {
        super(id);

        this.params = params;
    }

    @Override
    public String toString() {
        ToolStrBuilder strBuilder = new ToolStrBuilder();

        if (this.hasParameters()) {
            int lastParamIndex = (this.params.length - 1);
            String param;

            for (int a = 0; a < this.params.length; a++) {
                strBuilder.append(this.type.getId());

                if (!(param = this.params[a]).isEmpty()) {
                    strBuilder.append(((a == lastParamIndex) ? StringUtils.SPACE : ToolStringUtils.HYPHEN));
                    strBuilder.append(param);
                }

                strBuilder.append(ToolStringUtils.CRLF);
            }
        } else {
            strBuilder.append(this.type.getId());
            strBuilder.append(ToolStringUtils.CRLF);
        }

        return strBuilder.build();
    }

    @Override
    public boolean hasParameters() {
        return (this.params.length != 0);
    }

    @Override
    public String[] getParameters() {
        return this.params;
    }

    @Override
    public void setParameters(String ... params) {
        this.params = params;
    }
}
