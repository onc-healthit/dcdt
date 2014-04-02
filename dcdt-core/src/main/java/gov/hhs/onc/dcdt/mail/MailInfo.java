package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import javax.annotation.Nullable;
import javax.mail.MessagingException;

public interface MailInfo extends ToolBean {
    public boolean hasFrom() throws MessagingException;

    @Nullable
    public MailAddress getFrom() throws MessagingException;

    public boolean hasTo() throws MessagingException;

    @Nullable
    public MailAddress getTo() throws MessagingException;

    public boolean hasMessageHelper();

    @Nullable
    public ToolMimeMessageHelper getMessageHelper();

    public void setMessageHelper(@Nullable ToolMimeMessageHelper msgHelper);

    public boolean hasDecryptionErrorMessage();

    @Nullable
    public String getDecryptionErrorMessage();

    public void setDecryptionErrorMessage(@Nullable String decryptionErrorMsg);
}
