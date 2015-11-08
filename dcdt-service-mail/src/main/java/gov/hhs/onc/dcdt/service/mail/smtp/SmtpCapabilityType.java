package gov.hhs.onc.dcdt.service.mail.smtp;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;

public enum SmtpCapabilityType implements ToolIdentifier {
    EIGHT_BIT_MIME("8BITMIME"), AUTH(null), ENHANCED_STATUS_CODES("ENHANCEDSTATUSCODES"), HELP(null), PIPELINING(null), SIZE(null), START_TLS("STARTTLS");

    private final String id;

    private SmtpCapabilityType(@Nullable String id) {
        this.id = ObjectUtils.defaultIfNull(id, this.name());
    }

    @Override
    public String getId() {
        return this.id;
    }
}
