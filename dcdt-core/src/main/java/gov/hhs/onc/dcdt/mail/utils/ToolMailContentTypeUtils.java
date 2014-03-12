package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParameterList;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.MimeType;

public abstract class ToolMailContentTypeUtils {
    public static boolean isMultipart(ContentType contentType) {
        return (contentType.match(MailContentTypes.MULTIPART) || contentType.match(MailContentTypes.MULTIPART_MIXED));
    }

    public static boolean isSignature(ContentType contentType) {
        return (isDetachedSignature(contentType) || isMultipartSignature(contentType));
    }

    public static boolean isMultipartSignature(ContentType contentType) {
        return contentType.match(MailContentTypes.MULTIPART_SIGNED);
    }

    public static boolean isDetachedSignature(ContentType contentType) {
        return (contentType.match(MailContentTypes.APP_PKCS7_SIG) || contentType.match(MailContentTypes.APP_X_PKCS7_SIG));
    }

    public static boolean isSignedData(ContentType contentType) {
        return (contentType.match(MailContentTypes.APP_PKCS7_MIME_SIGNED) || contentType.match(MailContentTypes.APP_X_PKCS7_MIME_SIGNED));
    }

    public static boolean isEnvelopedData(ContentType contentType) {
        return (contentType.match(MailContentTypes.APP_PKCS7_MIME_ENV) || contentType.match(MailContentTypes.APP_X_PKCS7_MIME_ENV));
    }

    public static boolean containsDetachedSignature(ContentType contentType) {
        String protocolValue = contentType.getParameter(MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PARAM_NAME);

        return (protocolValue.equals(MailContentTypes.APP_PKCS7_SIG.getBaseType()) || protocolValue.equals(MailContentTypes.APP_X_PKCS7_SIG.getBaseType())
            && !contentType.getParameter(MailContentTypes.MULTIPART_SIGNED_MSG_DIGEST_ALG_PARAM_NAME).isEmpty());
    }

    @SuppressWarnings({ "unchecked" })
    public static ContentType getContentType(MimeType mimeType) {
        return getContentType(mimeType.getType(), mimeType.getSubtype(),
            ((Entry<String, String>[]) ToolCollectionUtils.toArray(mimeType.getParameters().entrySet(), Entry.class)));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static ContentType getContentType(String type, @Nullable Entry<String, String> ... paramEntries) {
        return getContentType(type, null, paramEntries);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static ContentType getContentType(String type, @Nullable String subtype, @Nullable Entry<String, String> ... paramEntries) {
        ParameterList params = new ParameterList();

        if (!ArrayUtils.isEmpty(paramEntries)) {
            // noinspection ConstantConditions
            for (Entry<String, String> paramEntry : paramEntries) {
                params.set(paramEntry.getKey(), paramEntry.getValue());
            }
        }

        return new ContentType(type, subtype, params);
    }
}
