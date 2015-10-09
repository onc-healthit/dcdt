package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;

public interface CertificateDescriptor extends CryptographyDescriptor {
    public boolean hasAia();

    @Nullable
    public AuthorityInformationAccess getAia();

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
    public CertificateName getSubjectName();

    public boolean hasValidInterval();

    @Nullable
    public CertificateValidInterval getValidInterval();
}
