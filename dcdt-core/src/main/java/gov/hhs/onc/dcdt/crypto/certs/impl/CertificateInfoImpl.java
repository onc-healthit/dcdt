package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;

@Embeddable
public class CertificateInfoImpl extends AbstractCryptographyDescriptor implements CertificateInfo {
    private X509Certificate cert;

    public CertificateInfoImpl() {
        this(null);
    }

    public CertificateInfoImpl(@Nullable X509Certificate cert) {
        this.cert = cert;
    }

    @Override
    public boolean hasCertificate() {
        return this.cert != null;
    }

    @Column(name = "cert_data", nullable = false)
    @Lob
    @Nullable
    @Override
    public X509Certificate getCertificate() {
        return this.cert;
    }

    @Override
    public void setCertificate(@Nullable X509Certificate cert) {
        this.cert = cert;
    }

    @Override
    @Transient
    public boolean isCertificateAuthority() {
        return this.hasCertificate() && new BasicConstraints(this.cert.getBasicConstraints()).isCA();
    }

    @Override
    public boolean hasCertificateType() {
        return this.getCertificateType() != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateType getCertificateType() {
        return this.hasCertificate() ? CertificateType.X509 : null;
    }

    @Override
    public boolean hasSerialNumber() {
        return ToolNumberUtils.isPositive(this.getSerialNumber());
    }

    @Nullable
    @Override
    @Transient
    public BigInteger getSerialNumber() {
        return this.hasCertificate() ? this.cert.getSerialNumber() : null;
    }

    @Override
    public boolean hasSignatureAlgorithm() {
        return this.getSignatureAlgorithm() != null;
    }

    @Nullable
    @Override
    @Transient
    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.hasCertificate() ? CryptographyUtils.findObjectId(SignatureAlgorithm.class, new ASN1ObjectIdentifier(this.cert.getSigAlgOID())) : null;
    }

    @Override
    public boolean hasSubject() {
        return this.getSubject() != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateName getSubject() {
        return this.hasCertificate() ? new CertificateNameImpl(this.cert.getSubjectDN().getName()) : null;
    }

    @Override
    public boolean hasValidInterval() {
        return this.getValidInterval() != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateValidInterval getValidInterval() {
        return this.hasCertificate() ? new CertificateValidIntervalImpl(this.cert.getNotBefore(), this.cert.getNotAfter()) : null;
    }
}
