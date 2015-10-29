package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;

public interface CertificateDescriptor<T extends CertificateIntervalDescriptor> extends CryptographyDescriptor {
    public boolean isIntermediateCertificateAuthority();

    public boolean isRootCertificateAuthority();

    public boolean isCertificateAuthority();

    public boolean hasCertificateType();

    @Nullable
    public CertificateType getCertificateType();

    public boolean hasCrlDistributionUris();

    @Nullable
    public Set<URI> getCrlDistributionUris();

    public boolean hasInterval();

    @Nullable
    public T getInterval();

    public boolean hasIssuerAccessUris();

    @Nullable
    public Set<URI> getIssuerAccessUris();

    public boolean hasKeyUsages();

    @Nullable
    public Set<KeyUsageType> getKeyUsages();

    public boolean isSelfIssued();

    public boolean hasSerialNumber();

    @Nullable
    public CertificateSerialNumber getSerialNumber();

    public boolean hasSignatureAlgorithm();

    @Nullable
    public SignatureAlgorithm getSignatureAlgorithm();

    public boolean hasSubjectAltNames();

    @Nullable
    public ToolMultiValueMap<GeneralNameType, ASN1Encodable> getSubjectAltNames();

    public boolean hasSubjectDn();

    @Nullable
    public CertificateDn getSubjectDn();
}
