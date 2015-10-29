package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalConfig;
import javax.annotation.Nonnegative;

public class CertificateIntervalConfigImpl extends AbstractCertificateIntervalDescriptor implements CertificateIntervalConfig {
    private long offset;

    @Override
    public void setDuration(@Nonnegative long duration) {
        this.duration = duration;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset(long offset) {
        this.offset = offset;
    }
}
