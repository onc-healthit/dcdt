package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.MimeType;

/**
 * Derived from:
 * <ul>
 * <li><a href="http://tools.ietf.org/html/rfc2046">RFC 2046 - Media Types</a></li>
 * <li><a href="http://tools.ietf.org/html/rfc5322">RFC 5322 - Internet Message Format</a></li>
 * <li><a href="http://tools.ietf.org/html/rfc5751#section-5">RFC 5751 - S/MIME 3.2 Message Specification, Section 5 - IANA Considerations</a></li>
 * <li><a href="http://wiki.directproject.org/Applicability+Statement+for+Secure+Health+Transport+Working+Version">Applicability Statement for Secure Health
 * Transport</a></li>
 * </ul>
 * 
 * A summary is available here: <a href="http://en.wikipedia.org/wiki/Internet_media_type">Internet_media_type</a>
 */
public final class MailContentTypes {
    public final static String NAME_PARAM_NAME = "name";
    public final static String NAME_SMIME_MIME_PARAM_VALUE = ToolStringUtils.quote("smime.p7m");
    public final static Pair<String, String> NAME_SMIME_MIME_PARAM = new ImmutablePair<>(NAME_PARAM_NAME, NAME_SMIME_MIME_PARAM_VALUE);
    public final static String NAME_SMIME_SIG_PARAM_VALUE = ToolStringUtils.quote("smime.p7s");
    public final static Pair<String, String> NAME_SMIME_SIG_PARAM = new ImmutablePair<>(NAME_PARAM_NAME, NAME_SMIME_SIG_PARAM_VALUE);

    public final static String SMIME_TYPE_PARAM_NAME = "smime-type";
    public final static String SMIME_TYPE_ENV_DATA_PARAM_VALUE = "enveloped-data";
    public final static Pair<String, String> SMIME_TYPE_ENV_DATA_PARAM = new ImmutablePair<>(SMIME_TYPE_PARAM_NAME, SMIME_TYPE_ENV_DATA_PARAM_VALUE);
    public final static String SMIME_TYPE_SIGNED_DATA_PARAM_VALUE = "signed-data";
    public final static Pair<String, String> SMIME_TYPE_SIGNED_DATA_PARAM = new ImmutablePair<>(SMIME_TYPE_PARAM_NAME, SMIME_TYPE_SIGNED_DATA_PARAM_VALUE);

    public final static String MICALG_PARAM_NAME = "micalg";

    public final static String APP_PKCS7_MIME_SUBTYPE = "pkcs7-mime";
    public final static MimeType APP_PKCS7_MIME_ENV = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKCS7_MIME_SUBTYPE, SMIME_TYPE_ENV_DATA_PARAM,
        NAME_SMIME_MIME_PARAM);
    public final static MimeType APP_PKCS7_MIME_SIGNED = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKCS7_MIME_SUBTYPE,
        SMIME_TYPE_SIGNED_DATA_PARAM);
    public final static String APP_PKCS7_MIME_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKCS7_MIME_ENV);

    public final static String APP_X_PKCS7_MIME_SUBTYPE = "x-" + APP_PKCS7_MIME_SUBTYPE;
    public final static MimeType APP_X_PKCS7_MIME_ENV = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_X_PKCS7_MIME_SUBTYPE,
        SMIME_TYPE_ENV_DATA_PARAM, NAME_SMIME_MIME_PARAM);
    public final static MimeType APP_X_PKCS7_MIME_SIGNED = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_X_PKCS7_MIME_SUBTYPE,
        SMIME_TYPE_SIGNED_DATA_PARAM);
    public final static String APP_X_PKCS7_MIME_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_X_PKCS7_MIME_ENV);

    public final static String APP_PKCS7_SIG_SUBTYPE = "pkcs7-signature";
    public final static MimeType APP_PKCS7_SIG = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_PKCS7_SIG_SUBTYPE, NAME_SMIME_SIG_PARAM);
    public final static String APP_PKCS7_SIG_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_PKCS7_SIG);

    public final static String APP_X_PKCS7_SIG_SUBTYPE = "x-" + APP_PKCS7_SIG_SUBTYPE;
    public final static MimeType APP_X_PKCS7_SIG = ToolMimeTypeUtils.valueOf(CoreContentTypes.APP_TYPE, APP_X_PKCS7_SIG_SUBTYPE, NAME_SMIME_SIG_PARAM);
    public final static String APP_X_PKCS7_SIG_BASETYPE = ToolMimeTypeUtils.getBaseType(APP_X_PKCS7_SIG);

    public final static String MSG_TYPE = "message";

    public final static String MSG_RFC822_SUBTYPE = "rfc822";
    public final static MimeType MSG_RFC822 = ToolMimeTypeUtils.valueOf(MSG_TYPE, MSG_RFC822_SUBTYPE);

    public final static String MULTIPART_TYPE = "multipart";

    public final static String MULTIPART_MIXED_SUBTYPE = "mixed";
    public final static MimeType MULTIPART_MIXED = ToolMimeTypeUtils.valueOf(MULTIPART_TYPE, MULTIPART_MIXED_SUBTYPE);

    public final static String MULTIPART_RELATED_SUBTYPE = "related";
    public final static MimeType MULTIPART_RELATED = ToolMimeTypeUtils.valueOf(MULTIPART_TYPE, MULTIPART_RELATED_SUBTYPE);

    public final static String MULTIPART_SIGNED_SUBTYPE = "signed";
    public final static String MULTIPART_SIGNED_PROTOCOL_PARAM_NAME = "protocol";
    public final static String MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG_PARAM_VALUE = ToolStringUtils.quote(APP_PKCS7_SIG_BASETYPE);
    public final static String MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG_PARAM_VALUE = ToolStringUtils.quote(APP_X_PKCS7_SIG_BASETYPE);
    public final static Pair<String, String> MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG_PARAM = new ImmutablePair<>(MULTIPART_SIGNED_PROTOCOL_PARAM_NAME,
        MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG_PARAM_VALUE);
    public final static Pair<String, String> MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG_PARAM = new ImmutablePair<>(MULTIPART_SIGNED_PROTOCOL_PARAM_NAME,
        MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG_PARAM_VALUE);
    public final static MimeType MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG = ToolMimeTypeUtils.valueOf(MULTIPART_TYPE, MULTIPART_SIGNED_SUBTYPE,
        MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG_PARAM);
    public final static MimeType MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG = ToolMimeTypeUtils.valueOf(MULTIPART_TYPE, MULTIPART_SIGNED_SUBTYPE,
        MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG_PARAM);

    private MailContentTypes() {
    }
}
