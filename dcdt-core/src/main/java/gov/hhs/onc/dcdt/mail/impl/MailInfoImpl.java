package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("mailInfoImpl")
@Lazy
@Scope("prototype")
public class MailInfoImpl extends AbstractToolBean implements MailInfo {
    private ToolMimeMessageHelper msgHelper;
    private String decryptionErrorMsg;

    @Override
    public boolean hasFrom() throws MessagingException {
        return this.getFrom() != null;
    }

    @Nullable
    @Override
    public MailAddress getFrom() throws MessagingException {
        return this.hasMessageHelper() ? this.msgHelper.getFrom() : null;
    }

    @Override
    public boolean hasTo() throws MessagingException {
        return this.getTo() != null;
    }

    @Nullable
    @Override
    public MailAddress getTo() throws MessagingException {
        return this.hasMessageHelper() ? this.msgHelper.getTo() : null;
    }

    @Override
    public boolean hasMessageHelper() {
        return this.msgHelper != null;
    }

    @Nullable
    @Override
    public ToolMimeMessageHelper getMessageHelper() {
        return this.msgHelper;
    }

    @Override
    public void setMessageHelper(@Nullable ToolMimeMessageHelper msgHelper) {
        this.msgHelper = msgHelper;
    }

    @Override
    public boolean hasDecryptionErrorMessage() {
        return !StringUtils.isBlank(this.decryptionErrorMsg);
    }

    @Nullable
    @Override
    public String getDecryptionErrorMessage() {
        return this.decryptionErrorMsg;
    }

    @Override
    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg) {
        this.decryptionErrorMsg = decryptionErrorMsg;
    }
}
