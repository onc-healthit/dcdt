package gov.hhs.onc.dcdt.crypto.credentials.impl;

import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;
import javax.annotation.Nullable;

public class CredentialConfigImpl extends AbstractCredentialDescriptor<KeyConfig, CertificateConfig> implements CredentialConfig {
    public CredentialConfigImpl() {
        super();
    }

    public CredentialConfigImpl(@Nullable KeyConfig keyPairConfig, @Nullable CertificateConfig certConfig) {
        super(keyPairConfig, certConfig);
    }
}
