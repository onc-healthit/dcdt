package gov.hhs.onc.dcdt.service.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCapabilityType;
import gov.hhs.onc.dcdt.service.mail.smtp.capability.SmtpCapability;

public abstract class AbstractSmtpCapability implements SmtpCapability {
    protected SmtpCapabilityType type;

    protected AbstractSmtpCapability(SmtpCapabilityType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.getId();
    }

    @Override
    public SmtpCapabilityType getType() {
        return this.type;
    }
}
