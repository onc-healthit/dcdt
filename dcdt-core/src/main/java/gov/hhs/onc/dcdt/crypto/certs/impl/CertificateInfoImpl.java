package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.utils.CertificateNameUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

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
    public boolean hasExtension(ASN1ObjectIdentifier oid) throws CertificateException {
        return (this.getExtension(oid) != null);
    }

    @Nullable
    @Override
    @Transient
    public Extension getExtension(ASN1ObjectIdentifier oid) throws CertificateException {
        X509CertificateHolder certHolder = this.getCertificateHolder();

        return ((certHolder != null) ? certHolder.getExtension(oid) : null);
    }

    @Nullable
    @Override
    @Transient
    public Extensions getExtensions() throws CertificateException {
        X509CertificateHolder certHolder = this.getCertificateHolder();

        return ((certHolder != null) ? certHolder.getExtensions() : null);
    }

    @Nullable
    @Override
    @Transient
    public X509CertificateHolder getCertificateHolder() throws CertificateException {
        try {
            return (this.hasCertificate() ? new JcaX509CertificateHolder(this.cert) : null);
        } catch (CertificateEncodingException e) {
            throw new CertificateException(String.format("Unable to get BouncyCastle X509v3 certificate (subj={%s}, issuer={%s}, serialNum=%s) holder.",
                this.cert.getSubjectX500Principal().getName(), this.cert.getIssuerX500Principal().getName(), this.getSerialNumber()), e);
        }
    }

    @Override
    @Transient
    public boolean isSelfIssued() throws CertificateException {
        // noinspection ConstantConditions
        return (this.hasCertificate() && this.getSubjectName().equals(this.getIssuerName()));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        CertificateInfo certInfo;

        return (this.hasCertificate() && (obj != null) && ToolClassUtils.isAssignable(obj.getClass(), CertificateInfo.class)
            && (certInfo = ((CertificateInfo) obj)).hasCertificate() && this.cert.equals(certInfo.getCertificate()));
    }

    @Override
    public int hashCode() {
        return (this.hasCertificate() ? this.cert.hashCode() : super.hashCode());
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
    public boolean hasIssuerName() {
        try {
            return (this.getIssuerName() != null);
        } catch (CertificateException ignored) {
            return false;
        }
    }

    @Nullable
    @Override
    @Transient
    public CertificateName getIssuerName() throws CertificateException {
        return (this.hasCertificate() ? new CertificateNameImpl(CertificateNameUtils.buildIssuerAltNames(this.cert), this.cert.getIssuerX500Principal()) : null);
    }

    @Override
    public boolean hasSerialNumber() {
        return (this.getSerialNumber() != null);
    }

    @Nullable
    @Override
    @Transient
    public CertificateSerialNumber getSerialNumber() {
        return this.hasCertificate() ? new CertificateSerialNumberImpl(this.cert.getSerialNumber()) : null;
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
    public boolean hasSubjectName() {
        try {
            return (this.getSubjectName() != null);
        } catch (CertificateException ignored) {
            return false;
        }
    }

    @Nullable
    @Override
    @Transient
    public CertificateName getSubjectName() throws CertificateException {
        return (this.hasCertificate()
            ? new CertificateNameImpl(CertificateNameUtils.buildSubjectAltNames(this.cert), this.cert.getSubjectX500Principal()) : null);
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
