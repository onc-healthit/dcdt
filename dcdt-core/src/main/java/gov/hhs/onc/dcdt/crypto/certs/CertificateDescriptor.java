package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import javax.annotation.Nullable;

public interface CertificateDescriptor extends CryptographyDescriptor {
    public boolean isCertificateAuthority();

    public boolean hasCertificateType();

    @Nullable
    public CertificateType getCertificateType();

    public boolean hasSerialNumber();

    @Nullable
    public CertificateSerialNumber getSerialNumber();

    public boolean hasSignatureAlgorithm();

    @Nullable
    public SignatureAlgorithm getSignatureAlgorithm();

    public boolean hasSubject();

    @Nullable
    public CertificateName getSubject();

    public boolean hasValidInterval();

    @Nullable
    public CertificateValidInterval getValidInterval();
}
