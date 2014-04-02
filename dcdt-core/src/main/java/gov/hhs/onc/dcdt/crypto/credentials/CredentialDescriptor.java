package gov.hhs.onc.dcdt.crypto.credentials;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyDescriptor;
import javax.annotation.Nullable;

public interface CredentialDescriptor<T extends KeyDescriptor, U extends CertificateDescriptor> extends CryptographyDescriptor {
    public boolean hasCertificateDescriptor();

    @Nullable
    public U getCertificateDescriptor();

    public void setCertificateDescriptor(@Nullable U certDesc);

    public boolean hasKeyDescriptor();

    @Nullable
    public T getKeyDescriptor();

    public void setKeyDescriptor(@Nullable T keyDesc);
}
