package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import javax.annotation.Nonnegative;

public interface CertificateIntervalDescriptor extends CryptographyDescriptor {
    @Nonnegative
    public long getDuration();
}
