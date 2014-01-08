package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairConfig;
import javax.annotation.Nullable;

public class CredentialConfigImpl extends AbstractCredentialDescriptor<KeyPairConfig, CertificateConfig> implements CredentialConfig {
    public CredentialConfigImpl() {
        super();
    }

    public CredentialConfigImpl(@Nullable KeyPairConfig keyPairConfig, @Nullable CertificateConfig certConfig) {
        super(keyPairConfig, certConfig);
    }
}
