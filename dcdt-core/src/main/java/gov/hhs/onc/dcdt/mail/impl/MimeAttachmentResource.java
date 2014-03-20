package gov.hhs.onc.dcdt.mail.impl;

import java.io.IOException;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class MimeAttachmentResource extends ByteArrayResource {
    private MimeType contentType;
    private String filename;

    public MimeAttachmentResource(MimeBodyPart bodyPart) throws IOException, MessagingException {
        this(IOUtils.toByteArray(bodyPart.getInputStream()), bodyPart.getDescription(), MimeTypeUtils.parseMimeType(bodyPart.getContentType()), bodyPart
            .getFileName());
    }

    public MimeAttachmentResource(byte[] content, @Nullable String desc, MimeType contentType, @Nullable String filename) {
        super(content, desc);

        this.contentType = contentType;
        this.filename = filename;
    }

    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @Nullable
    public MimeType getContentType() {
        return this.contentType;
    }

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.getDescription());
    }

    public boolean hasFilename() {
        return !StringUtils.isBlank(this.filename);
    }

    @Nullable
    @Override
    public String getFilename() {
        return this.filename;
    }
}
