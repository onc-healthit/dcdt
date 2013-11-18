package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.crypto.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.KeyPairInfo;

public class CredentialInfoImpl implements CredentialInfo {
    private CertificateInfo certificateInfo;
    private KeyPairInfo keyPairInfo;

    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certificateInfo;
    }

    @Override
    public void setCertificateInfo(CertificateInfo certificateInfo) {
        this.certificateInfo = certificateInfo;
    }

    @Override
    public KeyPairInfo getKeyPairInfo() {
        return this.keyPairInfo;
    }

    @Override
    public void setKeyPairInfo(KeyPairInfo keyPairInfo) {
        this.keyPairInfo = keyPairInfo;
    }
}
