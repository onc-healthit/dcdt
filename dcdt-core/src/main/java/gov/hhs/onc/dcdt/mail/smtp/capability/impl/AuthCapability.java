package gov.hhs.onc.dcdt.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.mail.smtp.SmtpAuthMechanism;
import gov.hhs.onc.dcdt.mail.smtp.capability.SmtpCapabilityType;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class AuthCapability extends AbstractSmtpCapability {
    private SmtpAuthMechanism[] mechanisms;

    public AuthCapability(SmtpAuthMechanism ... mechanisms) {
        super(SmtpCapabilityType.AUTH);

        this.mechanisms = mechanisms;
    }

    @Override
    public String toString() {
        return (super.toString() + (this.hasMechanisms() ? (StringUtils.SPACE + Stream.of(this.mechanisms).map(ToolIdentifier::getId)
            .collect(Collectors.joining(StringUtils.SPACE))) : StringUtils.EMPTY));
    }

    public boolean hasMechanisms() {
        return (this.mechanisms.length > 0);
    }

    public SmtpAuthMechanism[] getMechanisms() {
        return this.mechanisms;
    }
}
