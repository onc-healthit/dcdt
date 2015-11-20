package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import javax.mail.internet.MimeUtility;

public enum MailEncoding implements ToolIdentifier {
    UTF_8(CharsetUtil.UTF_8);

    private final String charsetName;
    private final String mimeCharsetName;

    private MailEncoding(Charset charset) {
        this.mimeCharsetName = MimeUtility.mimeCharset((this.charsetName = charset.name()));
    }

    public String getCharsetName() {
        return this.charsetName;
    }

    @Override
    public String getId() {
        return this.name();
    }

    public String getMimeCharsetName() {
        return this.mimeCharsetName;
    }
}
