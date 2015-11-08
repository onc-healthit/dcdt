package gov.hhs.onc.dcdt.service.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpAuthType;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCapabilityType;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class AuthCapability extends AbstractSmtpCapability {
    private SmtpAuthType[] types;

    public AuthCapability(SmtpAuthType ... types) {
        super(SmtpCapabilityType.AUTH);

        this.types = types;
    }

    @Override
    public String toString() {
        return (super.toString() + (this.hasTypes() ? (StringUtils.SPACE + Stream.of(this.types).map(ToolIdentifier::getId)
            .collect(Collectors.joining(StringUtils.SPACE))) : StringUtils.EMPTY));
    }

    public boolean hasTypes() {
        return (this.types.length > 0);
    }

    public SmtpAuthType[] getTypes() {
        return this.types;
    }
}
