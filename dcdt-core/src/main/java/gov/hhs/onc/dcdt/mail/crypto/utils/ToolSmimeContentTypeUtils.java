package gov.hhs.onc.dcdt.mail.crypto.utils;

import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.crypto.MailDigestAlgorithm;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.EnumSet;
import javax.annotation.Nullable;
import org.springframework.util.MimeType;

public abstract class ToolSmimeContentTypeUtils {
    public static boolean isDetachedSignature(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_SIG, MailContentTypes.APP_X_PKCS7_SIG);
    }

    @Nullable
    public static MailDigestAlgorithm getMicalg(MimeType contentType) {
        if (ToolMimeTypeUtils.hasParameter(contentType, MailContentTypes.MICALG_PARAM_NAME)) {
            String micalgStr = ToolStringUtils.unquote(contentType.getParameter(MailContentTypes.MICALG_PARAM_NAME));

            for (MailDigestAlgorithm micalg : EnumSet.allOf(MailDigestAlgorithm.class)) {
                if (micalg.getMicalg().equals(micalgStr)) {
                    return micalg;
                }
            }
        }

        return null;
    }

    public static boolean isSignedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_MIME_SIGNED, MailContentTypes.APP_X_PKCS7_MIME_SIGNED);
    }

    public static boolean isMultipartSigned(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG,
            MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG);
    }

    public static boolean isEnvelopedData(MimeType contentType) {
        return ToolMimeTypeUtils.equals(contentType, MailContentTypes.APP_PKCS7_MIME_ENV, MailContentTypes.APP_X_PKCS7_MIME_ENV);
    }
}
