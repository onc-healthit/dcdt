package gov.hhs.onc.dcdt.crypto.credentials;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyDescriptor;
import javax.annotation.Nullable;

public interface CredentialDescriptor<T extends KeyDescriptor, U extends CertificateIntervalDescriptor, V extends CertificateDescriptor<U>> extends
    CryptographyDescriptor {
    public boolean hasCertificateDescriptor();

    @Nullable
    public V getCertificateDescriptor();

    public void setCertificateDescriptor(@Nullable V certDesc);

    public boolean hasKeyDescriptor();

    @Nullable
    public T getKeyDescriptor();

    public void setKeyDescriptor(@Nullable T keyDesc);
}
