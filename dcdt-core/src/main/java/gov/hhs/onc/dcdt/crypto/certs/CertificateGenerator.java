package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import javax.annotation.Nullable;

public interface CertificateGenerator extends CryptographyGenerator<CertificateConfig, CertificateInfo> {
    public CertificateInfo generateCertificate(KeyInfo keyPairInfo, CertificateConfig certConfig) throws CryptographyException;

    public CertificateInfo generateCertificate(@Nullable CredentialInfo issuerCredInfo, KeyInfo keyPairInfo, CertificateConfig certConfig)
        throws CryptographyException;
}
