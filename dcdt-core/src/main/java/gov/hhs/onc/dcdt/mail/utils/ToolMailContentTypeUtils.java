package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.utils.ToolMimeTypeUtils;
import org.springframework.util.MimeType;

public abstract class ToolMailContentTypeUtils {
    public static boolean isSignature(MimeType contentType) {
        return (isDetachedSignature(contentType) || isMultipartSignature(contentType));
    }

    public static boolean isMultipartSignature(MimeType contentType) {
        return (ToolMimeTypeUtils.equals(true, contentType, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG,
            MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG) && ToolMimeTypeUtils.hasParameter(contentType,
            MailContentTypes.MULTIPART_SIGNED_MSG_DIGEST_ALG_PARAM_NAME));
    }

    public static boolean isDetachedSignature(MimeType contentType) {
        return ToolMimeTypeUtils.equals(true, contentType, MailContentTypes.APP_PKCS7_SIG, MailContentTypes.APP_X_PKCS7_SIG);
    }

    public static boolean isSignedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(true, contentType, MailContentTypes.APP_PKCS7_MIME_SIGNED, MailContentTypes.APP_X_PKCS7_MIME_SIGNED);
    }

    public static boolean isEnvelopedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(true, contentType, MailContentTypes.APP_PKCS7_MIME_ENV, MailContentTypes.APP_X_PKCS7_MIME_ENV);
    }
}
