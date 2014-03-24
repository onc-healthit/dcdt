package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolMimeTypeUtils.MimeTypeComparator;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class ToolMimeMessageHelper extends MimeMessageHelper {
    public static class MimeAttachmentTransformer extends AbstractToolTransformer<MimeBodyPart, MimeAttachmentResource> {
        public final static MimeAttachmentTransformer INSTANCE = new MimeAttachmentTransformer();

        @Override
        protected MimeAttachmentResource transformInternal(MimeBodyPart bodyPart) throws Exception {
            return new MimeAttachmentResource(bodyPart);
        }
    }

    public static class MimeAttachmentPartFilenamePredicate extends AbstractToolPredicate<MimeBodyPart> {
        private String filename;

        public MimeAttachmentPartFilenamePredicate(String filename) {
            this.filename = filename;
        }

        @Override
        protected boolean evaluateInternal(MimeBodyPart bodyPart) throws Exception {
            return (MimeAttachmentPartPredicate.INSTANCE.evaluateInternal(bodyPart) && Objects.equals(bodyPart.getFileName(), this.filename));
        }
    }

    public static class MimeAttachmentPartPredicate extends AbstractToolPredicate<MimeBodyPart> {
        public final static MimeAttachmentPartPredicate INSTANCE = new MimeAttachmentPartPredicate();

        @Override
        protected boolean evaluateInternal(MimeBodyPart bodyPart) throws Exception {
            return Objects.equals(bodyPart.getDisposition(), MimeBodyPart.ATTACHMENT);
        }
    }

    public static class MimeBodyPartTransformer extends AbstractToolTransformer<MimePart, MimeBodyPart> {
        public final static MimeBodyPartTransformer INSTANCE = new MimeBodyPartTransformer();

        @Override
        protected MimeBodyPart transformInternal(MimePart part) throws Exception {
            return ((MimeBodyPart) part);
        }
    }

    public ToolMimeMessageHelper(Session mailSession, Charset mailEnc) throws MessagingException {
        this(new MimeMessage(mailSession), MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, mailEnc);
    }

    public ToolMimeMessageHelper(MimeMessage mimeMsg) throws IOException, MessagingException {
        this(mimeMsg, MimeMessageHelper.MULTIPART_MODE_NO);

        this.initializeMimeMultiparts();
    }

    public ToolMimeMessageHelper(MimeMessage mimeMsg, int multipartMode) throws MessagingException {
        super(mimeMsg, multipartMode);
    }

    public ToolMimeMessageHelper(MimeMessage mimeMsg, Charset mailEnc) throws IOException, MessagingException {
        this(mimeMsg, MimeMessageHelper.MULTIPART_MODE_NO, mailEnc);

        this.initializeMimeMultiparts();
    }

    private ToolMimeMessageHelper(MimeMessage mimeMsg, int multipartMode, Charset mailEnc) throws MessagingException {
        super(mimeMsg, multipartMode, mailEnc.name());
    }

    public Map<MimeType, MimePart> mapRootParts() throws MessagingException {
        return this.mapRootParts(false);
    }

    public Map<MimeType, MimePart> mapRootParts(boolean matchParams) throws MessagingException {
        return this.mapParts(true, matchParams);
    }

    public Map<MimeType, MimePart> mapParts() throws MessagingException {
        return this.mapParts(false);
    }

    public Map<MimeType, MimePart> mapParts(boolean matchParams) throws MessagingException {
        return this.mapParts(false, matchParams);
    }

    public Map<MimeType, MimePart> mapParts(boolean fromRoot, boolean matchParams) throws MessagingException {
        List<MimePart> parts = this.getParts(fromRoot);
        Map<MimeType, MimePart> partMap = new TreeMap<>((matchParams ? MimeTypeComparator.INSTANCE : MimeTypeComparator.INSTANCE_BASE_TYPE));

        for (MimePart part : parts) {
            partMap.put(getContentType(part), part);
        }

        return partMap;
    }

    public static MimeType getContentType(MimePart mimePart) throws MessagingException {
        return MimeTypeUtils.parseMimeType(mimePart.getContentType());
    }

    public boolean hasAttachments() throws MessagingException {
        return !this.getAttachments().isEmpty();
    }

    public Collection<MimeAttachmentResource> getAttachments() throws MessagingException {
        return CollectionUtils.collect(this.getAttachmentParts(), MimeAttachmentTransformer.INSTANCE);
    }

    public void setAttachments(@Nullable Iterable<MimeAttachmentResource> attachments) throws MessagingException {
        for (MimeBodyPart attachmentPart : this.getAttachmentParts()) {
            attachmentPart.getParent().removeBodyPart(attachmentPart);
        }

        if (attachments != null) {
            String attachmentFilename;

            for (MimeAttachmentResource attachment : attachments) {
                this.addAttachment((attachmentFilename = attachment.getFilename()), attachment, Objects.toString(attachment.getContentType(), null));

                if (attachment.hasDescription()) {
                    // noinspection ConstantConditions
                    this.getAttachmentPart(attachmentFilename).setDescription(attachment.getDescription());
                }
            }
        }
    }

    public boolean hasAttachmentPart(String filename) throws MessagingException {
        return (this.getAttachmentPart(filename) != null);
    }

    @Nullable
    public MimeBodyPart getAttachmentPart(String filename) throws MessagingException {
        return CollectionUtils.find(this.getAttachmentParts(), new MimeAttachmentPartFilenamePredicate(filename));
    }

    public boolean hasAttachmentParts() throws MessagingException {
        return !this.getAttachmentParts().isEmpty();
    }

    public Collection<MimeBodyPart> getAttachmentParts() throws MessagingException {
        return CollectionUtils.select(CollectionUtils.collect(
            CollectionUtils.select(this.getRootParts(), PredicateUtils.instanceofPredicate(MimeBodyPart.class)), MimeBodyPartTransformer.INSTANCE),
            MimeAttachmentPartPredicate.INSTANCE);
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

    @SuppressWarnings({ "unchecked" })
    public List<String> getHeaderLines() throws MessagingException {
        return EnumerationUtils.toList(((Enumeration<String>) this.getMimeMessage().getAllHeaderLines()));
    }

    @SuppressWarnings({ "unchecked" })
    public Set<Header> getHeaders() throws MessagingException {
        return new LinkedHashSet<>(EnumerationUtils.toList(((Enumeration<Header>) this.getMimeMessage().getAllHeaders())));
    }

    public void setHeaders(@Nullable Set<Header> headers) throws MessagingException {
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

    public List<MimePart> getRootParts() throws MessagingException {
        return this.getParts(true);
    }

    public List<MimePart> getParts() throws MessagingException {
        return this.getParts(false);
    }

    public List<MimePart> getParts(boolean fromRoot) throws MessagingException {
        if (!this.isMultipart()) {
            return ToolArrayUtils.asList(((MimePart) this.getMimeMessage()));
        } else if (!fromRoot && !this.hasMimeMultipart()) {
            return new ArrayList<>();
        }

        MimeMultipart mimeMultipart = (fromRoot ? this.getRootMimeMultipart() : this.getMimeMultipart());
        int numParts = mimeMultipart.getCount();
        List<MimePart> parts = new ArrayList<>(numParts);

        for (int a = 0; a < numParts; a++) {
            parts.add(((MimePart) mimeMultipart.getBodyPart(a)));
        }

        return parts;
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

    public boolean hasText() throws IOException, MessagingException {
        return !StringUtils.isBlank(this.getText());
    }

    @Nullable
    public String getText() throws IOException, MessagingException {
        Map<MimeType, MimePart> partMap = this.mapParts();

        return Objects.toString(
            (partMap.containsKey(MimeTypeUtils.TEXT_HTML) ? partMap.get(MimeTypeUtils.TEXT_HTML).getContent() : (partMap.containsKey(MimeTypeUtils.TEXT_PLAIN)
                ? partMap.get(MimeTypeUtils.TEXT_PLAIN).getContent() : null)), null);
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

    public MimeType getContentType() throws MessagingException {
        return getContentType(this.getMimeMessage());
    }

    private void initializeMimeMultiparts() throws IOException, MessagingException {
        if (!ToolMimeTypeUtils.equals(false, this.getContentType(), MailContentTypes.MULTIPART_MIXED)) {
            return;
        }

        MimeMultipart rootMimeMultipart = ((MimeMultipart) this.getMimeMessage().getContent());
        this.setMimeMultiparts(rootMimeMultipart, null);

        Map<MimeType, MimePart> rootPartMap = this.mapRootParts();

        if (rootPartMap.containsKey(MailContentTypes.MULTIPART_RELATED)) {
            this.setMimeMultiparts(rootMimeMultipart, ((MimeMultipart) rootPartMap.get(MailContentTypes.MULTIPART_RELATED).getContent()));
        }
    }
}
