package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import java.math.BigInteger;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface CrlConfig extends CrlDescriptor<CrlEntryConfig>, CryptographyConfig {
    @NotNull(message = "{dcdt.crypto.crl.validation.constraints.crl.type.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CrlType getCrlType();

    public void setCrlType(@Nullable CrlType crlType);

    public void setEntries(@Nullable CrlEntryConfig ... entries);

    @NotNull(message = "{dcdt.crypto.crl.validation.constraints.crl.issuer.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public CertificateDn getIssuerDn();

    public void setIssuerDn(@Nullable CertificateDn issuerDn);

    public void setNumber(@Nullable BigInteger num);

    @NotNull(message = "{dcdt.crypto.crl.validation.constraints.crl.sig.alg.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm();

    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg);
}
