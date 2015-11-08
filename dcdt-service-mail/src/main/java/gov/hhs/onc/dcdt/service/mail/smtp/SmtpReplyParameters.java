package gov.hhs.onc.dcdt.service.mail.smtp;

import io.netty.util.CharsetUtil;
import org.apache.commons.codec.binary.Base64;

public final class SmtpReplyParameters {
    public final static String AUTH_ID_PROMPT = Base64.encodeBase64String("Username:".getBytes(CharsetUtil.US_ASCII));
    public final static String AUTH_SECRET_PROMPT = Base64.encodeBase64String("Password:".getBytes(CharsetUtil.US_ASCII));

    public final static String ESMTP = "ESMTP";

    public final static String OK = "OK";

    private SmtpReplyParameters() {
    }
}
