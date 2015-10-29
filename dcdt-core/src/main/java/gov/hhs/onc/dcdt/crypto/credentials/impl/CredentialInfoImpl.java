package gov.hhs.onc.dcdt.crypto.credentials.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyInfoImpl;
import javax.annotation.Nullable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import org.hibernate.annotations.Target;

@Embeddable
public class CredentialInfoImpl extends AbstractCredentialDescriptor<KeyInfo, CertificateIntervalInfo, CertificateInfo> implements CredentialInfo {
    public CredentialInfoImpl() {
        super();
    }

    public CredentialInfoImpl(@Nullable KeyInfo keyInfo, @Nullable CertificateInfo certInfo) {
        super(keyInfo, certInfo);
    }

    @Embedded
    @Nullable
    @Override
    @Target(CertificateInfoImpl.class)
    public CertificateInfo getCertificateDescriptor() {
        return super.getCertificateDescriptor();
    }

    @Embedded
    @Nullable
    @Override
    @Target(KeyInfoImpl.class)
    public KeyInfo getKeyDescriptor() {
        return super.getKeyDescriptor();
    }
}
