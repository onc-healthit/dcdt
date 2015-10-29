package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalDescriptor;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import javax.annotation.Nonnegative;

public abstract class AbstractCertificateIntervalDescriptor extends AbstractCryptographyDescriptor implements CertificateIntervalDescriptor {
    protected long duration;

    @Nonnegative
    @Override
    public long getDuration() {
        return this.duration;
    }
}
