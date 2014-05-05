package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils.MimeTypeComparator;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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

    public static Map<String, MimeBodyPart> mapAttachmentPartFileNames(MimeMultipart multipart) throws MessagingException {
        int numBodyParts = multipart.getCount();
        Map<String, MimeBodyPart> attachmentPartFileNameMap = new LinkedHashMap<>(numBodyParts);
        MimeBodyPart bodyPart;

        for (int a = 0; a < numBodyParts; a++) {
            if (isAttachment((bodyPart = ((MimeBodyPart) multipart.getBodyPart(a))))) {
                attachmentPartFileNameMap.put(bodyPart.getFileName(), bodyPart);
            }
        }

        return attachmentPartFileNameMap;
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

    public static Map<MimeType, List<MimeBodyPart>> mapBodyPartContentTypes(MimeMultipart multipart) throws MessagingException {
        return mapBodyPartContentTypes(multipart, true);
    }

    public static Map<MimeType, List<MimeBodyPart>> mapBodyPartContentTypes(MimeMultipart multipart, boolean compareContentBaseType) throws MessagingException {
        Map<MimeType, List<MimeBodyPart>> bodyPartContentTypeMap =
            new TreeMap<>((compareContentBaseType ? MimeTypeComparator.INSTANCE_BASE_TYPE : MimeTypeComparator.INSTANCE));
        MimeBodyPart bodyPart;
        MimeType bodyPartContentType;

        for (int a = 0; a < multipart.getCount(); a++) {
            if (!bodyPartContentTypeMap.containsKey((bodyPartContentType = getContentType((bodyPart = ((MimeBodyPart) multipart.getBodyPart(a))))))) {
                bodyPartContentTypeMap.put((compareContentBaseType ? ToolMimeTypeUtils.forBaseType(bodyPartContentType) : bodyPartContentType),
                    new ArrayList<MimeBodyPart>());
            }

            bodyPartContentTypeMap.get(bodyPartContentType).add(bodyPart);
        }

        return bodyPartContentTypeMap;
    }

    public static List<MimeBodyPart> getBodyParts(MimeMultipart multipart) throws MessagingException {
        int numBodyParts = multipart.getCount();
        List<MimeBodyPart> bodyParts = new ArrayList<>(numBodyParts);

        for (int a = 0; a < numBodyParts; a++) {
            bodyParts.add(((MimeBodyPart) multipart.getBodyPart(a)));
        }

        return bodyParts;
    }

    @Nullable
    public static MailContentTransferEncoding getContentXferEncoding(MimePart part) throws MessagingException {
        String contentXferEncStr = part.getEncoding();

        if (contentXferEncStr != null) {
            for (MailContentTransferEncoding contentXferEnc : EnumSet.allOf(MailContentTransferEncoding.class)) {
                if (contentXferEnc.getValue().equals(contentXferEncStr)) {
                    return contentXferEnc;
                }
            }
        }

        return null;
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
