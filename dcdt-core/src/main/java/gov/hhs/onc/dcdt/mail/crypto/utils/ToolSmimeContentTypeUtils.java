package gov.hhs.onc.dcdt.mail.crypto.utils;

import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import org.springframework.util.MimeType;

public abstract class ToolSmimeContentTypeUtils {
    public static boolean isDetachedSignature(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_SIG, MailContentTypes.APP_X_PKCS7_SIG);
    }
    
    public static boolean isSignedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_MIME_SIGNED, MailContentTypes.APP_X_PKCS7_MIME_SIGNED);
    }
    
    public static boolean isMultipartSigned(MimeType contentType) {
        return (ToolMimeTypeUtils.equals(contentType, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG,
            MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG) && ToolMimeTypeUtils.hasParameter(contentType,
            MailContentTypes.MULTIPART_SIGNED_MSG_DIGEST_ALG_PARAM_NAME));
        
        // TODO: implement Message Integrity Check algorithm (micalg) content type parameter value validation
    }

    public static boolean isEnvelopedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_MIME_ENV, MailContentTypes.APP_X_PKCS7_MIME_ENV);
    }
}
