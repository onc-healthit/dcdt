package gov.hhs.onc.dcdt.service.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCapabilityType;

public class StartTlsCapability extends AbstractSmtpCapability {
    public StartTlsCapability() {
        super(SmtpCapabilityType.START_TLS);
    }
}
