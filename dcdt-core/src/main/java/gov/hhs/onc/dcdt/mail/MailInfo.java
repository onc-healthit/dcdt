package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolInfoBean;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.springframework.util.MimeType;

public interface MailInfo extends ToolInfoBean {
    public byte[] write() throws MessagingException;

    public boolean hasAttachments();

    @Nullable
    public List<MimeAttachmentResource> getAttachments();

    public void setAttachments(@Nullable List<MimeAttachmentResource> attachments) throws MessagingException;

    public boolean hasContentTransferEncoding();

    @Nullable
    public MailContentTransferEncoding getContentTransferEncoding();

    public boolean hasContentType();

    @Nullable
    public MimeType getContentType();

    public MailEncoding getEncoding();

    public boolean hasFrom();

    @Nullable
    public MailAddress getFrom();

    public void setFrom(@Nullable MailAddress fromAddr) throws MessagingException;

    public ToolMimeMessage getMessage();

    public boolean hasMessageId();

    @Nullable
    public String getMessageId();

    public boolean hasReplyTo();

    @Nullable
    public MailAddress[] getReplyTo();

    public void setReplyTo(@Nullable MailAddress ... replyToAddrs) throws MessagingException;

    public boolean hasSentDate();

    @Nullable
    public Date getSentDate();

    public void setSentDate(@Nullable Date sentDate) throws MessagingException;

    public boolean hasSubject();

    @Nullable
    public String getSubject();

    public void setSubject(@Nullable String subj) throws MessagingException;

    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(String text) throws MessagingException;

    public boolean hasTo();

    @Nullable
    public MailAddress getTo();

    public void setTo(@Nullable MailAddress toAddr) throws MessagingException;

    public boolean hasRecipients();

    public boolean hasRecipients(MailRecipientType recipType);

    public Map<MailRecipientType, MailAddress[]> getRecipients();

    @Nullable
    public MailAddress[] getRecipients(MailRecipientType recipType);

    public void setRecipients(Map<MailRecipientType, MailAddress[]> recipAddrs) throws MessagingException;

    public void setRecipients(MailRecipientType recipType, @Nullable MailAddress ... recipAddrs) throws MessagingException;
}
