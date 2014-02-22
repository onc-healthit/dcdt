package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;

public interface CertificateInfo extends CertificateDescriptor, CryptographyInfo {
    public boolean hasCertificate();

    @Nullable
    public X509Certificate getCertificate();

    public void setCertificate(@Nullable X509Certificate cert);
}
