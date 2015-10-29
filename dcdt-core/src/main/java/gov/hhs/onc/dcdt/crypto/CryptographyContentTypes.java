package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import org.springframework.util.MimeType;

public final class CryptographyContentTypes {
    public final static String APP_PKCS7_MIME_SUBTYPE = "pkcs7-mime";
    public final static MimeType APP_PKCS7_MIME = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKCS7_MIME_SUBTYPE);
    public final static String APP_PKCS7_MIME_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKCS7_MIME);

    public final static String APP_X_PKCS7_MIME_SUBTYPE = "x-" + APP_PKCS7_MIME_SUBTYPE;
    public final static MimeType APP_X_PKCS7_MIME = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_X_PKCS7_MIME_SUBTYPE);
    public final static String APP_X_PKCS7_MIME_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_X_PKCS7_MIME);

    public final static String APP_PKCS7_SIG_SUBTYPE = "pkcs7-signature";
    public final static MimeType APP_PKCS7_SIG = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKCS7_SIG_SUBTYPE);
    public final static String APP_PKCS7_SIG_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKCS7_SIG);

    public final static String APP_X_PKCS7_SIG_SUBTYPE = "x-" + APP_PKCS7_SIG_SUBTYPE;
    public final static MimeType APP_X_PKCS7_SIG = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_X_PKCS7_SIG_SUBTYPE);
    public final static String APP_X_PKCS7_SIG_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_X_PKCS7_SIG);

    public final static String APP_PKIX_CERT_SUBTYPE = "pkix-cert";
    public final static MimeType APP_PKIX_CERT = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKIX_CERT_SUBTYPE);
    public final static String APP_PKIX_CERT_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKIX_CERT);

    public final static String APP_PKIX_CRL_SUBTYPE = "pkix-crl";
    public final static MimeType APP_PKIX_CRL = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKIX_CRL_SUBTYPE);
    public final static String APP_PKIX_CRL_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKIX_CRL);

    private CryptographyContentTypes() {
    }
}
