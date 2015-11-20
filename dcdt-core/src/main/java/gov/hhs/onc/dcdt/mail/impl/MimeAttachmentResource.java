package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.MailEncoding;
import gov.hhs.onc.dcdt.mail.MailHeaders;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeType;

public class MimeAttachmentResource extends ByteArrayResource {
    private class MimeAttachmentDataSource implements DataSource {
        private String encName;

        public MimeAttachmentDataSource(String encName) {
            this.encName = encName;
        }

        @Override
        public String getContentType() {
            return Objects.toString(MimeAttachmentResource.this.contentType, null);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return MimeAttachmentResource.this.getInputStream();
        }

        @Override
        public String getName() {
            return this.encName;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException(String.format("Mail MIME attachment (name=%s) is read-only.", this.encName));
        }
    }

    private String desc;
    private MailContentTransferEncoding contentXferEnc;
    private MimeType contentType;
    private String filename;

    public MimeAttachmentResource(byte[] content, @Nullable String desc, @Nullable MailContentTransferEncoding contentXferEnc, MimeType contentType,
        @Nullable String filename) {
        super(content, desc);

        this.desc = Objects.toString(desc, StringUtils.EMPTY);
        this.contentXferEnc = contentXferEnc;
        this.contentType = contentType;
        this.filename = filename;
    }

    public MimeBodyPart toBodyPart(MailEncoding enc) throws MessagingException {
        boolean descAvailable = this.hasDescription(), filenameAvailable = this.hasFilename();
        String mimeCharsetName = enc.getMimeCharsetName(), encName = ObjectUtils.identityToString(this), encDesc = null, encFilename = null;

        if (descAvailable) {
            try {
                encName = (encDesc = MimeUtility.encodeText(this.desc, mimeCharsetName, null));
            } catch (UnsupportedEncodingException e) {
                throw new ToolMailException(String.format("Unable to encode (charset=%s) mail MIME attachment description: %s", mimeCharsetName, this.desc), e);
            }
        }

        if (filenameAvailable) {
            try {
                encName = (encFilename = MimeUtility.encodeText(this.filename, mimeCharsetName, null));
            } catch (UnsupportedEncodingException e) {
                throw new ToolMailException(String.format("Unable to encode (charset=%s) mail MIME attachment file name: %s", mimeCharsetName, this.filename),
                    e);
            }
        }

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(new MimeAttachmentDataSource(encName)));
        bodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

        if (descAvailable) {
            bodyPart.setDescription(encDesc);
        }

        if (filenameAvailable) {
            bodyPart.setFileName(encFilename);
        }

        if (this.hasContentTransferEncoding()) {
            bodyPart.setHeader(MailHeaders.CONTENT_TRANSFER_ENCODING_NAME, this.contentXferEnc.getId());
        }

        return bodyPart;
    }

    public boolean hasContentTransferEncoding() {
        return (this.contentXferEnc != null);
    }

    @Nullable
    public MailContentTransferEncoding getContentTransferEncoding() {
        return this.contentXferEnc;
    }

    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @Nullable
    public MimeType getContentType() {
        return this.contentType;
    }

    public boolean hasDescription() {
        return !this.desc.isEmpty();
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    public boolean hasFilename() {
        return (this.filename != null);
    }

    @Nullable
    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public boolean equals(Object obj) {
        MimeAttachmentResource mimeAttachmentResource;

        return ((obj != null) && (this == obj) && ToolClassUtils.isAssignable(obj.getClass(), MimeAttachmentResource.class) && (new EqualsBuilder()
            .appendSuper(super.equals((mimeAttachmentResource = (MimeAttachmentResource) obj)))
            .append(this.contentXferEnc, mimeAttachmentResource.getContentTransferEncoding()).append(this.contentType, mimeAttachmentResource.getContentType())
            .append(this.filename, mimeAttachmentResource.getFilename()).isEquals()));
    }
}
