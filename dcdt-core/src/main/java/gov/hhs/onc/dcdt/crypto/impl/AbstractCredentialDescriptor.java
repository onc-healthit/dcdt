package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.crypto.CredentialDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairDescriptor;
import javax.annotation.Nullable;

public abstract class AbstractCredentialDescriptor<T extends KeyPairDescriptor, U extends CertificateDescriptor> extends AbstractCryptographyDescriptor
    implements CredentialDescriptor<T, U> {
    protected T keyPairDesc;
    protected U certDesc;

    protected AbstractCredentialDescriptor() {
        this(null, null);
    }

    protected AbstractCredentialDescriptor(@Nullable T keyPairDesc, @Nullable U certDesc) {
        this.keyPairDesc = keyPairDesc;
        this.certDesc = certDesc;
    }

    @Override
    public boolean hasCertificateDescriptor() {
        return this.certDesc != null;
    }

    @Nullable
    @Override
    public U getCertificateDescriptor() {
        return this.certDesc;
    }

    @Override
    public void setCertificateDescriptor(@Nullable U certDesc) {
        this.certDesc = certDesc;
    }

    @Override
    public boolean hasKeyPairDescriptor() {
        return this.keyPairDesc != null;
    }

    @Nullable
    @Override
    public T getKeyPairDescriptor() {
        return this.keyPairDesc;
    }

    @Override
    public void setKeyPairDescriptor(@Nullable T keyPairDesc) {
        this.keyPairDesc = keyPairDesc;
    }
}
