package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.bouncycastle.asn1.ASN1Encodable;

public interface CertificateConfig extends CertificateDescriptor<CertificateIntervalConfig>, CryptographyConfig {
    public void setCertificateAuthority(boolean ca);

    @NotNull(message = "{dcdt.crypto.certs.validation.constraints.cert.type.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateType getCertificateType();

    public void setCertificateType(@Nullable CertificateType certType);

    public void setCrlDistributionUris(@Nullable Set<URI> crlDistribUris);

    @NotNull(message = "{dcdt.crypto.certs.validation.constraints.cert.interval.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateIntervalConfig getInterval();

    public void setInterval(@Nullable CertificateIntervalConfig interval);

    public void setIssuerAccessUris(@Nullable Set<URI> issuerAccessUris);

    public void setKeyUsages(@Nullable Set<KeyUsageType> keyUsages);

    public void setSelfIssued(boolean selfIssued);

    public void setSerialNumber(@Nullable CertificateSerialNumber serialNum);

    @NotNull(message = "{dcdt.crypto.certs.validation.constraints.cert.sig.alg.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm();

    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg);

    public void setSubjectAltNames(@Nullable ToolMultiValueMap<GeneralNameType, ASN1Encodable> subjAltNames);

    @NotNull(message = "{dcdt.crypto.certs.validation.constraints.cert.subj.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateDn getSubjectDn();

    public void setSubjectDn(@Nullable CertificateDn subjDn);
}
