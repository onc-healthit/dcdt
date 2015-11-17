package gov.hhs.onc.dcdt.mail.smtp.capability.impl;

import gov.hhs.onc.dcdt.mail.smtp.capability.SmtpCapabilityType;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.StringUtils;

public class SizeCapability extends AbstractSmtpCapability {
    private long size;

    public SizeCapability(@Nonnegative long size) {
        super(SmtpCapabilityType.SIZE);

        this.size = size;
    }

    @Override
    public String toString() {
        return (super.toString() + StringUtils.SPACE + this.size);
    }
}
