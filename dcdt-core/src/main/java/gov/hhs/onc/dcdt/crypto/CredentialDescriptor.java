package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairDescriptor;
import javax.annotation.Nullable;

public interface CredentialDescriptor<T extends KeyPairDescriptor, U extends CertificateDescriptor> extends CryptographyDescriptor {
    public boolean hasCertificateDescriptor();

    @Nullable
    public U getCertificateDescriptor();

    public void setCertificateDescriptor(@Nullable U certDesc);

    public boolean hasKeyPairDescriptor();

    @Nullable
    public T getKeyPairDescriptor();

    public void setKeyPairDescriptor(@Nullable T keyPairDesc);
}
