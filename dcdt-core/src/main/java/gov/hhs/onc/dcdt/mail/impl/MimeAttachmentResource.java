package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeType;

public class MimeAttachmentResource extends ByteArrayResource {
    private MimeType contentType;
    private MailContentTransferEncoding contentXferEnc;
    private String filename;

    public MimeAttachmentResource(byte[] content, @Nullable String desc, MimeType contentType, @Nullable MailContentTransferEncoding contentXferEnc,
        @Nullable String filename) {
        super(content, desc);

        this.contentType = contentType;
        this.contentXferEnc = contentXferEnc;
        this.filename = filename;
    }

    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @Nullable
    public MimeType getContentType() {
        return this.contentType;
    }

    public boolean hasContentXferEncoding() {
        return (this.contentXferEnc != null);
    }

    @Nullable
    public MailContentTransferEncoding getContentXferEncoding() {
        return this.contentXferEnc;
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && ToolClassUtils.isAssignable(obj.getClass(), MimeAttachmentResource.class)) {
            MimeAttachmentResource mimeAttachmentResource = (MimeAttachmentResource) obj;

            return super.equals(mimeAttachmentResource) && Objects.equals(this.contentType, mimeAttachmentResource.getContentType()) && Objects
                .equals(this.contentXferEnc, mimeAttachmentResource.getContentXferEncoding()) && Objects
                .equals(this.filename, mimeAttachmentResource.getFilename());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
