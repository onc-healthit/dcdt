package gov.hhs.onc.dcdt.mail.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptorBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.MailEncoding;
import gov.hhs.onc.dcdt.mail.MailHeaderNames;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.MailRecipientType;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class MailInfoImpl extends AbstractToolDescriptorBean implements MailInfo {
    private class ToolMimeMessageHelper extends MimeMessageHelper {
        public ToolMimeMessageHelper(boolean processMultiparts) throws MessagingException {
            super(MailInfoImpl.this.msg, (processMultiparts ? MULTIPART_MODE_NO : MULTIPART_MODE_MIXED_RELATED), MailInfoImpl.this.enc.getMimeCharset().name());

            if (processMultiparts) {
                this.processMultiparts();
            }
        }

        private void processMultiparts() throws MessagingException {
            if (!ToolMimeTypeUtils.equals(false, ToolMimePartUtils.getContentType(MailInfoImpl.this.msg), MailContentTypes.MULTIPART_MIXED)) {
                return;
            }

            MimeMultipart rootMultipart;

            try {
                rootMultipart = ((MimeMultipart) MailInfoImpl.this.msg.getContent());
            } catch (Exception e) {
                throw new ToolMailException(String.format("Unable to process mail MIME message (id=%s, from=%s, to=%s) root MIME multipart.",
                    MailInfoImpl.this.msg.getMessageID(), ToolArrayUtils.getFirst(MailInfoImpl.this.msg.getFrom()),
                    ToolArrayUtils.getFirst(MailInfoImpl.this.msg.getRecipients(RecipientType.TO))), e);
            }

            MimeBodyPart mainBodyPart = findPart(MailContentTypes.MULTIPART_RELATED, ToolMimePartUtils.getBodyParts(rootMultipart));
            MimeMultipart mainMultipart = null;

            if (mainBodyPart != null) {
                try {
                    mainMultipart = ((MimeMultipart) mainBodyPart.getContent());
                } catch (Exception e) {
                    throw new ToolMailException(String.format("Unable to process mail MIME message (id=%s, from=%s, to=%s) main MIME multipart.",
                        MailInfoImpl.this.msg.getMessageID(), ToolArrayUtils.getFirst(MailInfoImpl.this.msg.getFrom()),
                        ToolArrayUtils.getFirst(MailInfoImpl.this.msg.getRecipients(RecipientType.TO))), e);
                }
            }

            this.setMimeMultiparts(rootMultipart, mainMultipart);
        }
    }

    private ToolMimeMessage msg;
    private MailEncoding enc;
    private ToolMimeMessageHelper msgHelper;
    private List<MimeAttachmentResource> attachments;
    private MailContentTransferEncoding contentXferEnc;
    private MimeType contentType;
    private MailAddress fromAddr;
    private String msgId;
    private Map<MailRecipientType, MailAddress[]> recipAddrs = new HashMap<>(MailRecipientType.values().length);
    private MailAddress[] replyToAddrs;
    private Date sentDate;
    private String subj;
    private String text;

    public MailInfoImpl(Session session, MailEncoding enc) throws MessagingException {
        this(new ToolMimeMessage(session), enc, false);
    }

    public MailInfoImpl(ToolMimeMessage msg, MailEncoding enc) throws MessagingException {
        this(msg, enc, true);
    }

    private MailInfoImpl(ToolMimeMessage msg, MailEncoding enc, boolean processMultiparts) throws MessagingException {
        this.msg = msg;
        this.enc = enc;
        this.msgHelper = new ToolMimeMessageHelper(processMultiparts);

        this.msg.saveChanges();

        this.parseMessage();
    }

    @Override
    public byte[] write() throws MessagingException {
        try {
            byte[] msgContent = ToolMimePartUtils.write(this.msg);

            this.parseMessage();

            return msgContent;
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to write mail MIME message (id=%s, from=%s, to=%s).", this.msgId, this.fromAddr, this.getTo()), e);
        }
    }

    @Override
    protected void reset() {
        this.attachments = null;
        this.contentXferEnc = null;
        this.contentType = null;
        this.fromAddr = null;
        this.msgId = null;
        this.recipAddrs.clear();
        this.replyToAddrs = null;
        this.sentDate = null;
        this.subj = null;
        this.text = null;
    }

    @Nullable
    private static MailAddress[] parseMessageAddresses(Address ... msgAddrs) throws MessagingException {
        if (ArrayUtils.isEmpty(msgAddrs)) {
            return null;
        }

        MailAddress[] addrs = new MailAddress[msgAddrs.length];

        for (int a = 0; a < msgAddrs.length; a++) {
            addrs[a] = new MailAddressImpl(msgAddrs[a].toString());
        }

        return addrs;
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private static <T extends MimePart> T findPart(MimeType contentType, T ... parts) throws MessagingException {
        for (T part : parts) {
            if (ToolMimeTypeUtils.equals(false, ToolMimePartUtils.getContentType(part), contentType)) {
                return part;
            }
        }

        return null;
    }

    private void parseMessage() throws MessagingException {
        this.reset();

        this.msgId = this.msg.getMessageID();
        this.fromAddr = ToolArrayUtils.getFirst(this.parseHeaderAddresses(MailHeaderNames.FROM));

        MailAddress[] recipMsgAddrs;

        for (MailRecipientType recipType : MailRecipientType.values()) {
            if ((recipMsgAddrs = parseMessageAddresses(this.msg.getRecipients(recipType.getType()))) != null) {
                recipAddrs.put(recipType, recipMsgAddrs);
            }
        }

        MimePart[] parts = null;

        if (this.msgHelper.isMultipart()) {
            List<MimeBodyPart> attachmentParts = ToolMimePartUtils.getAttachmentParts(this.msgHelper.getRootMimeMultipart());

            if (!attachmentParts.isEmpty()) {
                this.attachments = new ArrayList<>(attachmentParts.size());

                for (MimeBodyPart attachmentPart : attachmentParts) {
                    try {
                        this.attachments.add(new MimeAttachmentResource(ToolMimePartUtils.write(attachmentPart), attachmentPart.getDescription(),
                            ToolMimePartUtils.getContentTransferEncoding(attachmentPart), ToolMimePartUtils.getContentType(attachmentPart), attachmentPart
                                .getFileName()));
                    } catch (Exception e) {
                        throw new ToolMailException(String.format("Unable to write mail MIME message (id=%s, from=%s, to=%s) attachment MIME body part.",
                            this.msgId, this.fromAddr, this.getTo()), e);
                    }
                }
            }

            MimeMultipart mainMultipart = this.msgHelper.getMimeMultipart();

            if (mainMultipart != null) {
                parts = ToolMimePartUtils.getBodyParts(mainMultipart);
            }
        } else {
            parts = ArrayUtils.toArray(this.msg);
        }

        if (!ArrayUtils.isEmpty(parts)) {
            MimePart textPart = findPart(MimeTypeUtils.TEXT_HTML, parts);

            if (textPart == null) {
                textPart = findPart(MimeTypeUtils.TEXT_PLAIN, parts);
            }

            if (textPart != null) {
                try {
                    this.text = Objects.toString(textPart.getContent(), null);
                } catch (Exception e) {
                    throw new ToolMailException(String.format("Unable to get mail MIME message (id=%s, from=%s, to=%s) text MIME part content.", this.msgId,
                        this.fromAddr, this.getTo()), e);
                }
            }
        }

        this.contentXferEnc = ToolMimePartUtils.getContentTransferEncoding(this.msg);
        this.contentType = ToolMimePartUtils.getContentType(this.msg);
        this.replyToAddrs = this.parseHeaderAddresses(MailHeaderNames.REPLY_TO);
        this.sentDate = this.msg.getSentDate();
        this.subj = this.msg.getSubject();
    }

    @Nullable
    private Address[] buildMessageAddresses(MailAddress ... addrs) throws MessagingException {
        if (ArrayUtils.isEmpty(addrs)) {
            return null;
        }

        Address[] msgAddrs = new Address[addrs.length];

        for (int a = 0; a < addrs.length; a++) {
            msgAddrs[a] = addrs[a].toInternetAddress(this.enc);
        }

        return msgAddrs;
    }

    @Nullable
    private MailAddress[] parseHeaderAddresses(String headerName) throws MessagingException {
        String headerValues = this.msg.getHeader(headerName, ToolStringUtils.COMMA);

        if (StringUtils.isEmpty(headerValues)) {
            return null;
        }

        return parseMessageAddresses(InternetAddress.parseHeader(headerValues, true));
    }

    @Override
    public boolean hasAttachments() {
        return !CollectionUtils.isEmpty(this.attachments);
    }

    @Nullable
    @Override
    public List<MimeAttachmentResource> getAttachments() {
        return this.attachments;
    }

    @Override
    public void setAttachments(@Nullable List<MimeAttachmentResource> attachments) throws MessagingException {
        MimeMultipart rootMultipart = this.msgHelper.getRootMimeMultipart();

        for (MimeBodyPart attachmentPart : ToolMimePartUtils.getAttachmentParts(rootMultipart)) {
            attachmentPart.getParent().removeBodyPart(attachmentPart);
        }

        if (!CollectionUtils.isEmpty(attachments)) {
            for (MimeAttachmentResource attachment : attachments) {
                rootMultipart.addBodyPart(attachment.toBodyPart(this.enc));
            }
        }

        this.attachments = attachments;
    }

    @Override
    public boolean hasText() {
        return (this.text != null);
    }

    @Nullable
    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) throws MessagingException {
        this.msgHelper.setText(text, true);
        this.text = text;
    }

    @Override
    public boolean hasContentTransferEncoding() {
        return (this.contentXferEnc != null);
    }

    @Nullable
    @Override
    public MailContentTransferEncoding getContentTransferEncoding() {
        return this.contentXferEnc;
    }

    @Override
    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @Nullable
    @Override
    public MimeType getContentType() {
        return this.contentType;
    }

    @Override
    public MailEncoding getEncoding() {
        return this.enc;
    }

    @Override
    public boolean hasFrom() {
        return (this.fromAddr != null);
    }

    @Nullable
    @Override
    public MailAddress getFrom() {
        return this.fromAddr;
    }

    @Override
    public void setFrom(@Nullable MailAddress fromAddr) throws MessagingException {
        Address msgFromAddr = null;

        if (fromAddr != null) {
            msgFromAddr = fromAddr.toInternetAddress(this.enc);
        }

        this.msg.setFrom(msgFromAddr);
        this.fromAddr = fromAddr;
    }

    @Override
    public ToolMimeMessage getMessage() {
        return this.msg;
    }

    @Override
    public boolean hasMessageId() {
        return (this.msgId != null);
    }

    @Nullable
    @Override
    public String getMessageId() {
        return this.msgId;
    }

    @Override
    public boolean hasReplyTo() {
        return !ArrayUtils.isEmpty(this.replyToAddrs);
    }

    @Nullable
    @Override
    public MailAddress[] getReplyTo() {
        return this.replyToAddrs;
    }

    @Override
    public void setReplyTo(@Nullable MailAddress ... replyToAddrs) throws MessagingException {
        this.msg.setReplyTo(this.buildMessageAddresses(replyToAddrs));
        this.replyToAddrs = replyToAddrs;
    }

    @Override
    public boolean hasSentDate() {
        return (this.getSentDate() != null);
    }

    @Nullable
    @Override
    public Date getSentDate() {
        return this.sentDate;
    }

    @Override
    public void setSentDate(@Nullable Date sentDate) throws MessagingException {
        this.msg.setSentDate(sentDate);
        this.sentDate = sentDate;
    }

    @Override
    public boolean hasSubject() {
        return (this.getSubject() != null);
    }

    @Nullable
    @Override
    public String getSubject() {
        return this.subj;
    }

    @Override
    public void setSubject(@Nullable String subj) throws MessagingException {
        this.msg.setSubject(subj, this.enc.getMimeCharset().name());
        this.subj = subj;
    }

    @Override
    public boolean hasTo() {
        return this.hasRecipients(MailRecipientType.TO);
    }

    @Nullable
    @Override
    public MailAddress getTo() {
        return ToolArrayUtils.getFirst(this.getRecipients(MailRecipientType.TO));
    }

    @Override
    public void setTo(@Nullable MailAddress toAddr) throws MessagingException {
        this.setRecipients(MailRecipientType.TO, ArrayUtils.toArray(toAddr));
    }

    @Override
    public boolean hasRecipients() {
        return !this.recipAddrs.isEmpty();
    }

    @Override
    public boolean hasRecipients(MailRecipientType recipType) {
        return !ArrayUtils.isEmpty(this.getRecipients(recipType));
    }

    @Override
    public Map<MailRecipientType, MailAddress[]> getRecipients() {
        MailRecipientType[] recipTypes = MailRecipientType.values();
        Map<MailRecipientType, MailAddress[]> recipAddrs = new HashMap<>(recipTypes.length);
        MailAddress[] recipMsgAddrs;

        for (MailRecipientType recipType : recipTypes) {
            if ((recipMsgAddrs = this.getRecipients(recipType)) != null) {
                recipAddrs.put(recipType, recipMsgAddrs);
            }
        }

        return recipAddrs;
    }

    @Nullable
    @Override
    public MailAddress[] getRecipients(MailRecipientType recipType) {
        return this.recipAddrs.get(recipType);
    }

    @Override
    public void setRecipients(Map<MailRecipientType, MailAddress[]> recipAddrs) throws MessagingException {
        for (MailRecipientType recipType : MailRecipientType.values()) {
            this.setRecipients(recipType, recipAddrs.get(recipType));
        }
    }

    @Override
    public void setRecipients(MailRecipientType recipType, @Nullable MailAddress ... recipAddrs) throws MessagingException {
        this.msg.setRecipients(recipType.getType(), this.buildMessageAddresses(recipAddrs));

        if (ArrayUtils.isEmpty(recipAddrs)) {
            this.recipAddrs.remove(recipType);
        } else {
            this.recipAddrs.put(recipType, recipAddrs);
        }
    }
}
