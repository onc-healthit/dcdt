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
    private ToolMimeMessageHelper encryptedMsgHelper;
    private String decryptionErrorMsg;

    @Override
    public boolean hasFrom() throws MessagingException {
        return this.getFrom() != null;
    }

    @Nullable
    @Override
    public MailAddress getFrom() throws MessagingException {
        return this.hasEncryptedMessageHelper() ? this.encryptedMsgHelper.getFrom() : null;
    }

    @Override
    public boolean hasTo() throws MessagingException {
        return this.getTo() != null;
    }

    @Nullable
    @Override
    public MailAddress getTo() throws MessagingException {
        return this.hasEncryptedMessageHelper() ? this.encryptedMsgHelper.getTo() : null;
    }

    @Override
    public boolean hasEncryptedMessageHelper() {
        return this.encryptedMsgHelper != null;
    }

    @Nullable
    @Override
    public ToolMimeMessageHelper getEncryptedMessageHelper() {
        return this.encryptedMsgHelper;
    }

    @Override
    public void setEncryptedMessageHelper(@Nullable ToolMimeMessageHelper encryptedMsgHelper) {
        this.encryptedMsgHelper = encryptedMsgHelper;
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
