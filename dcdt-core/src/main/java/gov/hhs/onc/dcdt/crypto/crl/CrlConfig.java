package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import javax.annotation.Nullable;

public interface CrlConfig extends CrlDescriptor<CrlEntryConfig>, CryptographyConfig {
    public void setEntries(@Nullable CrlEntryConfig ... entries);

    public void setIssuerDn(@Nullable CertificateDn issuerDn);

    // @NotNull(message = "{dcdt.crypto.crl.validation.constraints.crl.sig.alg.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm();

    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg);
}
