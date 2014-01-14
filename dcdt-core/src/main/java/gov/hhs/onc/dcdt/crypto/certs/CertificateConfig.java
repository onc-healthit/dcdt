package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import java.math.BigInteger;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface CertificateConfig extends CertificateDescriptor, CryptographyConfig {
    public void setCertificateAuthority(boolean ca);

    @NotNull(message = "{dcdt.validation.constraints.crypto.certs.cert.type.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateType getCertificateType();

    public void setCertificateType(@Nullable CertificateType certType);

    public void setSerialNumber(@Nullable BigInteger serialNum);

    @NotNull(message = "{dcdt.validation.constraints.crypto.certs.cert.sig.alg.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm();

    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg);

    @NotNull(message = "{dcdt.validation.constraints.crypto.certs.cert.subj.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateName getSubject();

    public void setSubject(@Nullable CertificateName subj);

    @NotNull(message = "{dcdt.validation.constraints.crypto.certs.cert.valid.interval.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateValidInterval getValidInterval();

    public void setValidInterval(@Nullable CertificateValidInterval validInterval);
}
