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

    public boolean hasSubjectName();

    @Nullable
    public CertificateName getSubjectName() throws CertificateException;

    public boolean hasValidInterval();

    @Nullable
    public CertificateValidInterval getValidInterval();
}
