package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;

public interface CertificateInfo extends CertificateDescriptor, CryptographyInfo {
    public boolean hasExtension(ASN1ObjectIdentifier oid) throws CertificateException;
    
    @Nullable
    public Extension getExtension(ASN1ObjectIdentifier oid) throws CertificateException;
    
    @Nullable
    public Extensions getExtensions() throws CertificateException;
    
    @Nullable
    public X509CertificateHolder getCertificateHolder() throws CertificateException;
    
    public boolean isSelfIssued() throws CertificateException;

    public boolean hasCertificate();

    @Nullable
    public X509Certificate getCertificate();

    public void setCertificate(@Nullable X509Certificate cert);

    public boolean hasIssuerName();

    @Nullable
    public CertificateName getIssuerName() throws CertificateException;
}
