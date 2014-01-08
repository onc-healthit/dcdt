package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import javax.annotation.Nullable;

public class CredentialInfoImpl extends AbstractCredentialDescriptor<KeyPairInfo, CertificateInfo> implements CredentialInfo {
    public CredentialInfoImpl() {
        super();
    }

    public CredentialInfoImpl(@Nullable KeyPairInfo keyPairInfo, @Nullable CertificateInfo certInfo) {
        super(keyPairInfo, certInfo);
    }
}
