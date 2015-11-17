package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import javax.mail.internet.MimeUtility;

public enum MailEncoding implements ToolIdentifier {
    US_ASCII(CharsetUtil.US_ASCII), UTF_8(CharsetUtil.UTF_8);

    private final Charset charset;
    private final Charset mimeCharset;

    private MailEncoding(Charset charset) {
        this.mimeCharset = Charset.forName(MimeUtility.mimeCharset((this.charset = charset).name()));
    }

    public Charset getCharset() {
        return this.charset;
    }

    @Override
    public String getId() {
        return this.name();
    }

    public Charset getMimeCharset() {
        return this.mimeCharset;
    }
}
