package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils.MimeTypeComparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.annotation.Nullable;
import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class ToolMimeMessageHelper extends MimeMessageHelper {
    public final static String FILE_EXT_MAIL = ".eml";

    public ToolMimeMessageHelper(Session mailSession, Charset mailEnc) throws MessagingException {
        this(new MimeMessage(mailSession), MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, mailEnc);
    }

    public ToolMimeMessageHelper(MimeMessage msg, Charset mailEnc) throws IOException, MessagingException {
        this(msg, MimeMessageHelper.MULTIPART_MODE_NO, mailEnc);

        this.initializeMimeMultiparts();
    }

    private ToolMimeMessageHelper(MimeMessage msg, int multipartMode, Charset mailEnc) throws MessagingException {
        super(msg, multipartMode, MimeUtility.mimeCharset(mailEnc.name()));
    }

    public String writeString() throws IOException, MessagingException {
        return this.writeString(true);
    }

    public String writeString(boolean includeHeaders) throws IOException, MessagingException {
        ToolStrBuilder strBuilder = new ToolStrBuilder();

        if (includeHeaders) {
            strBuilder.appendWithDelimiters(this.getHeaderLines(), StringUtils.LF);
        }

        strBuilder.appendWithDelimiter(new String(this.write(), this.getEncoding()), StringUtils.LF);

        return strBuilder.toString();
    }

    public byte[] write() throws IOException, MessagingException {
        return this.write(true);
    }

    public byte[] write(boolean raw) throws IOException, MessagingException {
        MimeMessage msg = this.getMimeMessage();

        return IOUtils.toByteArray((raw ? msg.getRawInputStream() : msg.getInputStream()));
    }

    public boolean hasText() throws IOException, MessagingException {
        return !StringUtils.isBlank(this.getText());
    }

    @Nullable
    public String getText() throws IOException, MessagingException {
        Map<MimeType, List<MimePart>> partMap = this.mapPartContentTypes();

        // noinspection ConstantConditions
        return Objects.toString((partMap.containsKey(MimeTypeUtils.TEXT_HTML)
            ? ToolListUtils.getFirst(partMap.get(MimeTypeUtils.TEXT_HTML)).getContent() : (partMap.containsKey(MimeTypeUtils.TEXT_PLAIN) ? ToolListUtils
                .getFirst(partMap.get(MimeTypeUtils.TEXT_PLAIN)).getContent() : null)), null);
    }

    public void setAttachments(@Nullable Iterable<MimeAttachmentResource> attachmentResources) throws MessagingException {
        for (MimeBodyPart attachmentPart : this.getAttachmentParts()) {
            attachmentPart.getParent().removeBodyPart(attachmentPart);
        }

        if (attachmentResources != null) {
            String attachmentFileName;

            for (MimeAttachmentResource attachmentResource : attachmentResources) {
                this.addAttachment((attachmentFileName = attachmentResource.getFilename()), attachmentResource,
                    Objects.toString(attachmentResource.getContentType(), null));

                if (attachmentResource.hasDescription()) {
                    // noinspection ConstantConditions
                    this.mapAttachmentPartFileNames().get(attachmentFileName).setDescription(attachmentResource.getDescription());
                }
            }
        }
    }

    public Map<String, MimeBodyPart> mapAttachmentPartFileNames() throws MessagingException {
        return (isMultipart() ? ToolMimePartUtils.mapAttachmentPartFileNames(this.getRootMimeMultipart()) : new LinkedHashMap<String, MimeBodyPart>());
    }

    public List<MimeBodyPart> getAttachmentParts() throws MessagingException {
        return this.getAttachmentParts(true);
    }

    public List<MimeBodyPart> getAttachmentParts(boolean requireFileName) throws MessagingException {
        return (isMultipart() ? ToolMimePartUtils.getAttachmentParts(this.getRootMimeMultipart()) : new ArrayList<MimeBodyPart>());
    }

    public Map<MimeType, List<MimePart>> mapPartContentTypes() throws MessagingException {
        return mapPartContentTypes(true);
    }

    public Map<MimeType, List<MimePart>> mapPartContentTypes(boolean compareContentBaseType) throws MessagingException {
        return this.mapPartContentTypes(false, compareContentBaseType);
    }

    public Map<MimeType, List<MimePart>> mapPartContentTypes(boolean fromRoot, boolean compareContentBaseType) throws MessagingException {
        List<? extends MimePart> parts = this.getParts(fromRoot);
        Map<MimeType, List<MimePart>> partContentTypeMap =
            new TreeMap<>((compareContentBaseType ? MimeTypeComparator.INSTANCE_BASE_TYPE : MimeTypeComparator.INSTANCE));
        MimeType partContentType;

        for (MimePart part : parts) {
            if (!partContentTypeMap.containsKey((partContentType = ToolMimePartUtils.getContentType(part)))) {
                partContentTypeMap.put((compareContentBaseType ? ToolMimeTypeUtils.forBaseType(partContentType) : partContentType), new ArrayList<MimePart>());
            }

            partContentTypeMap.get(partContentType).add(part);
        }

        return partContentTypeMap;
    }

    public List<? extends MimePart> getParts() throws MessagingException {
        return this.getParts(false);
    }

    public List<? extends MimePart> getParts(boolean fromRoot) throws MessagingException {
        if (!this.isMultipart()) {
            return ToolArrayUtils.asList(this.getMimeMessage());
        } else if (!fromRoot && !this.hasMimeMultipart()) {
            return new ArrayList<>();
        } else {
            return ToolMimePartUtils.getBodyParts((fromRoot ? this.getRootMimeMultipart() : this.getMimeMultipart()));
        }
    }

    @Nullable
    public MailContentTransferEncoding getContentXferEncoding() throws MessagingException {
        return ToolMimePartUtils.getContentXferEncoding(this.getMimeMessage());
    }

    public MimeType getContentType() throws MessagingException {
        return ToolMimePartUtils.getContentType(this.getMimeMessage());
    }

    @SuppressWarnings({ "unchecked" })
    public List<String> getHeaderLines() throws MessagingException {
        return EnumerationUtils.toList(((Enumeration<String>) this.getMimeMessage().getAllHeaderLines()));
    }

    @SuppressWarnings({ "unchecked" })
    public List<Header> getHeaders() throws MessagingException {
        return EnumerationUtils.toList(((Enumeration<Header>) this.getMimeMessage().getAllHeaders()));
    }

    public void setHeaders(@Nullable Collection<Header> headers) throws MessagingException {
        MimeMessage mimeMsg = this.getMimeMessage();

        for (Header header : this.getHeaders()) {
            mimeMsg.removeHeader(header.getName());
        }

        if (!CollectionUtils.isEmpty(headers)) {
            // noinspection ConstantConditions
            for (Header header : headers) {
                mimeMsg.setHeader(header.getName(), header.getValue());
            }
        }
    }

    public boolean hasMimeMultipart() {
        return (this.getMimeMultipart() != null);
    }

    public boolean hasTo() throws MessagingException {
        return (this.getTo() != null);
    }

    @Nullable
    public MailAddress getTo() throws MessagingException {
        Address addr = ToolArrayUtils.getFirst(this.getMimeMessage().getRecipients(RecipientType.TO));

        return ((addr != null) ? new MailAddressImpl(addr) : null);
    }

    public void setTo(@Nullable MailAddress to) throws MessagingException {
        this.getMimeMessage().setRecipient(RecipientType.TO, ((to != null) ? to.toInternetAddress() : null));
    }

    public boolean hasReplyTo() throws MessagingException {
        return (this.getReplyTo() != null);
    }

    @Nullable
    public MailAddress getReplyTo() throws MessagingException {
        Address addr = ToolArrayUtils.getFirst(this.getMimeMessage().getReplyTo());

        return ((addr != null) ? new MailAddressImpl(addr) : null);
    }

    public void setReplyTo(@Nullable MailAddress replyTo) throws MessagingException {
        this.getMimeMessage().setReplyTo(ArrayUtils.toArray(((replyTo != null) ? replyTo.toInternetAddress() : null)));
    }

    public boolean hasFrom() throws MessagingException {
        return (this.getFrom() != null);
    }

    @Nullable
    public MailAddress getFrom() throws MessagingException {
        Address addr = ToolArrayUtils.getFirst(this.getMimeMessage().getFrom());

        return ((addr != null) ? new MailAddressImpl(addr) : null);
    }

    public void setFrom(@Nullable MailAddress from) throws MessagingException {
        this.getMimeMessage().setFrom(((from != null) ? from.toInternetAddress() : null));
    }

    public boolean hasSubject() throws MessagingException {
        return this.getSubject() != null;
    }

    @Nullable
    public String getSubject() throws MessagingException {
        return this.getMimeMessage().getSubject();
    }

    public void setSubject(@Nullable String subject) throws MessagingException {
        this.getMimeMessage().setSubject(subject);
    }

    private void initializeMimeMultiparts() throws IOException, MessagingException {
        if (!ToolMimeTypeUtils.equals(false, this.getContentType(), MailContentTypes.MULTIPART_MIXED)) {
            return;
        }

        MimeMultipart rootMimeMultipart = ((MimeMultipart) this.getMimeMessage().getContent());
        this.setMimeMultiparts(rootMimeMultipart, null);

        Map<MimeType, List<MimePart>> rootPartMap = this.mapPartContentTypes(true, true);

        if (rootPartMap.containsKey(MailContentTypes.MULTIPART_RELATED)) {
            // noinspection ConstantConditions
            this.setMimeMultiparts(rootMimeMultipart,
                ((MimeMultipart) ToolListUtils.getFirst(rootPartMap.get(MailContentTypes.MULTIPART_RELATED)).getContent()));
        }
    }
}
