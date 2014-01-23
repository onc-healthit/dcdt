package gov.hhs.onc.dcdt.crypto.credentials.impl;

import gov.hhs.onc.dcdt.crypto.credentials.CredentialDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyDescriptor;
import javax.annotation.Nullable;

public abstract class AbstractCredentialDescriptor<T extends KeyDescriptor, U extends CertificateDescriptor> extends AbstractCryptographyDescriptor implements
    CredentialDescriptor<T, U> {
    protected T keyDesc;
    protected U certDesc;

    protected AbstractCredentialDescriptor() {
        this(null, null);
    }

    protected AbstractCredentialDescriptor(@Nullable T keyDesc, @Nullable U certDesc) {
        this.keyDesc = keyDesc;
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
    public boolean hasKeyDescriptor() {
        return this.keyDesc != null;
    }

    @Nullable
    @Override
    public T getKeyDescriptor() {
        return this.keyDesc;
    }

    @Override
    public void setKeyDescriptor(@Nullable T keyDesc) {
        this.keyDesc = keyDesc;
    }
}
