package gov.hhs.onc.dcdt.crypto.credentials;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;

public interface CredentialConfig extends CredentialDescriptor<KeyConfig, CertificateIntervalConfig, CertificateConfig>, CryptographyConfig {
}
