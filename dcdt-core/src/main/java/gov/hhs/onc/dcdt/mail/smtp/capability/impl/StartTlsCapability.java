package gov.hhs.onc.dcdt.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.mail.smtp.capability.SmtpCapabilityType;

public class StartTlsCapability extends AbstractSmtpCapability {
    public StartTlsCapability() {
        super(SmtpCapabilityType.STARTTLS);
    }
}
