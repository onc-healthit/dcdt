package gov.hhs.onc.dcdt.mail.smtp;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;

public enum SmtpAuthMechanism implements ToolIdentifier {
    PLAIN(null), LOGIN(null), GSSAPI(null), DIGEST_MD5("DIGEST-MD5"), MD5(null), CRAM_MD5("CRAM-MD5");

    private final String id;

    private SmtpAuthMechanism(@Nullable String id) {
        this.id = ObjectUtils.defaultIfNull(id, this.name());
    }

    @Override
    public String getId() {
        return this.id;
    }
}
