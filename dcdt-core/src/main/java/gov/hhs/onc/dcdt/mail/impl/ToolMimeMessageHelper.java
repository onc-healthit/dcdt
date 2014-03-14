package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.utils.ToolMailContentTypeUtils;
import gov.hhs.onc.dcdt.mail.utils.ToolMailContentTypeUtils.MailContentTypeComparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

public class ToolMimeMessageHelper extends MimeMessageHelper {
    public ToolMimeMessageHelper(Session mailSession, Charset mailEnc) throws MessagingException {
        this(new MimeMessage(mailSession), MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, mailEnc);
    }

    public ToolMimeMessageHelper(MimeMessage mimeMsg, Charset mailEnc) throws IOException, MessagingException {
        this(mimeMsg, MimeMessageHelper.MULTIPART_MODE_NO, mailEnc);

        this.initializeMimeMultiparts();
    }

    private ToolMimeMessageHelper(MimeMessage mimeMsg, int multipartMode, Charset mailEnc) throws MessagingException {
        super(mimeMsg, multipartMode, mailEnc.name());
    }

    public Map<ContentType, MimePart> mapRootParts() throws MessagingException {
        return this.mapRootParts(false);
    }

    public Map<ContentType, MimePart> mapRootParts(boolean matchParams) throws MessagingException {
        return this.mapParts(true, matchParams);
    }

    public Map<ContentType, MimePart> mapParts() throws MessagingException {
        return this.mapParts(false);
    }

    public Map<ContentType, MimePart> mapParts(boolean matchParams) throws MessagingException {
        return this.mapParts(false, matchParams);
    }

    public Map<ContentType, MimePart> mapParts(boolean fromRoot, boolean matchParams) throws MessagingException {
        List<MimePart> parts = this.getParts(fromRoot);
        Map<ContentType, MimePart> partMap =
            new TreeMap<>((matchParams ? MailContentTypeComparator.INSTANCE_MATCH_PARAMS : MailContentTypeComparator.INSTANCE));

        for (MimePart part : parts) {
            partMap.put(new ContentType(part.getContentType()), part);
        }

        return partMap;
    }

    private void initializeMimeMultiparts() throws IOException, MessagingException {
        MimeMessage mimeMsg = this.getMimeMessage();

        if (!ToolMailContentTypeUtils.isMultipartMixed(new ContentType(mimeMsg.getContentType()))) {
            return;
        }

        MimeMultipart rootMimeMultipart = ((MimeMultipart) this.getMimeMessage().getContent());
        this.setMimeMultiparts(rootMimeMultipart, null);

        Map<ContentType, MimePart> rootPartMap = this.mapRootParts();

        if (rootPartMap.containsKey(MailContentTypes.MULTIPART_RELATED)) {
            this.setMimeMultiparts(rootMimeMultipart, ((MimeMultipart) rootPartMap.get(MailContentTypes.MULTIPART_RELATED).getContent()));
        }
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

    public boolean hasText() throws IOException, MessagingException {
        return !StringUtils.isBlank(this.getText());
    }

    @Nullable
    public String getText() throws IOException, MessagingException {
        Map<ContentType, MimePart> partMap = this.mapParts();

        return Objects.toString(
            (partMap.containsKey(MailContentTypes.TEXT_HTML) ? partMap.get(MailContentTypes.TEXT_HTML).getContent() : (partMap
                .containsKey(MailContentTypes.TEXT_PLAIN) ? partMap.get(MailContentTypes.TEXT_PLAIN).getContent() : null)), null);
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
}
