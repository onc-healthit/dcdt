package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairConfig;

public interface CredentialConfig extends CredentialDescriptor<KeyPairConfig, CertificateConfig> {
}
