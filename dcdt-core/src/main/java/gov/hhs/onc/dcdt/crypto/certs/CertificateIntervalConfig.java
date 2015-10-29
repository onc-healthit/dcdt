package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import javax.annotation.Nonnegative;

public interface CertificateIntervalConfig extends CertificateIntervalDescriptor, CryptographyConfig {
    public void setDuration(@Nonnegative long duration);

    public long getOffset();

    public void setOffset(long offset);
}
