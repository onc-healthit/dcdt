package gov.hhs.onc.dcdt.crypto.credentials.impl;

import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import javax.annotation.Nullable;

public class CredentialInfoImpl extends AbstractCredentialDescriptor<KeyInfo, CertificateInfo> implements CredentialInfo {
    public CredentialInfoImpl() {
        super();
    }

    public CredentialInfoImpl(@Nullable KeyInfo keyInfo, @Nullable CertificateInfo certInfo) {
        super(keyInfo, certInfo);
    }
}
