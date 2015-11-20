package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public abstract class ToolMimePartUtils {
    public final static String DELIM_HEADER = ":";

    public static byte[] write(MimePart part) throws MessagingException {
        try (ByteArrayOutputStream dataOutStream = new ByteArrayOutputStream()) {
            part.writeTo(dataOutStream);

            return dataOutStream.toByteArray();
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to write MIME part (class=%s, desc=%s) data.", ToolClassUtils.getName(part),
                part.getDescription()), e);
        }
    }

    public static List<MimeBodyPart> getAttachmentParts(MimeMultipart multipart) throws MessagingException {
        return getAttachmentParts(multipart, true);
    }

    public static List<MimeBodyPart> getAttachmentParts(MimeMultipart multipart, boolean requireFileName) throws MessagingException {
        int numBodyParts = multipart.getCount();
        List<MimeBodyPart> attachmentParts = new ArrayList<>(numBodyParts);
        MimeBodyPart bodyPart;

        for (int a = 0; a < numBodyParts; a++) {
            if (isAttachment((bodyPart = ((MimeBodyPart) multipart.getBodyPart(a))), requireFileName)) {
                attachmentParts.add(bodyPart);
            }
        }

        return attachmentParts;
    }

    public static boolean isAttachment(MimeBodyPart bodyPart) throws MessagingException {
        return isAttachment(bodyPart, true);
    }

    public static boolean isAttachment(MimeBodyPart bodyPart, boolean requireFileName) throws MessagingException {
        return (Objects.equals(bodyPart.getDisposition(), MimeBodyPart.ATTACHMENT) && (!requireFileName || (bodyPart.getFileName() != null)));
    }

    public static MimeBodyPart[] getBodyParts(MimeMultipart multipart) throws MessagingException {
        int numBodyParts = multipart.getCount();
        MimeBodyPart[] bodyParts = new MimeBodyPart[numBodyParts];

        for (int a = 0; a < numBodyParts; a++) {
            bodyParts[a] = ((MimeBodyPart) multipart.getBodyPart(a));
        }

        return bodyParts;
    }

    @Nullable
    public static MailContentTransferEncoding getContentTransferEncoding(MimePart part) throws MessagingException {
        String contentXferEncStr = part.getEncoding();

        if (contentXferEncStr == null) {
            return null;
        }

        MailContentTransferEncoding contentXferEnc = ToolEnumUtils.findById(MailContentTransferEncoding.class, contentXferEncStr);

        if (contentXferEnc == null) {
            throw new ToolMailException(String.format("Unable to parse MIME part (class=%s, desc=%s) content transfer encoding: %s",
                ToolClassUtils.getName(part), part.getDescription(), contentXferEncStr));
        }

        return contentXferEnc;
    }

    @Nullable
    public static MimeType getContentType(MimePart part) throws MessagingException {
        String partContentType = part.getContentType();

        if (partContentType == null) {
            return null;
        }

        try {
            return MimeTypeUtils.parseMimeType(partContentType);
        } catch (InvalidMimeTypeException e) {
            throw new ToolMailException(String.format("Unable to parse MIME part (class=%s, desc=%s) content type: %s", ToolClassUtils.getName(part),
                part.getDescription(), partContentType), e);
        }
    }

    @Nullable
    public static MimeType getContentType(MimeMultipart multipart) throws MessagingException {
        String multipartContentType = multipart.getContentType();

        if (multipartContentType == null) {
            return null;
        }

        try {
            return MimeTypeUtils.parseMimeType(multipartContentType);
        } catch (InvalidMimeTypeException e) {
            throw new ToolMailException(String.format("Unable to parse MIME multipart (class=%s, body part number=%d) content type: %s",
                ToolClassUtils.getName(multipart), multipart.getCount(), multipartContentType), e);
        }
    }
}
