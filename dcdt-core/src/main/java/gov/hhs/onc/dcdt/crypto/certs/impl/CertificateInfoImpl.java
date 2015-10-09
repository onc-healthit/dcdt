package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
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
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

@Embeddable
public class CertificateInfoImpl extends AbstractCertificateDescriptor implements CertificateInfo {
    private X509Certificate cert;
    private X509CertificateHolder certHolder;
    private CertificateName issuerName;

    public CertificateInfoImpl() throws CertificateException {
        this(null);
    }

    public CertificateInfoImpl(@Nullable X509Certificate cert) throws CertificateException {
        this.setCertificate(cert);
    }

    @Override
    @Transient
    public boolean isSelfIssued() {
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
    protected void reset() {
        super.reset();

        this.certHolder = null;
        this.issuerName = null;
    }

    private void processCertificate() throws CertificateException {
        this.reset();

        if (!this.hasCertificate()) {
            return;
        }

        try {
            try {
                this.certHolder = new JcaX509CertificateHolder(this.cert);
            } catch (CertificateEncodingException e) {
                throw new CertificateException(String.format("Unable to build BouncyCastle X509v3 certificate (subj={%s}, issuer={%s}, serialNum=%s) holder.",
                    this.cert.getSubjectX500Principal().getName(), this.cert.getIssuerX500Principal().getName(), this.getSerialNumber()), e);
            }

            if (this.hasExtension(Extension.authorityInfoAccess)) {
                this.aia = AuthorityInformationAccess.getInstance(this.getExtension(Extension.authorityInfoAccess));
            }

            this.ca = new BasicConstraints(this.cert.getBasicConstraints()).isCA();
            this.certType = CertificateType.X509;
            this.issuerName = new CertificateNameImpl(CertificateNameUtils.buildIssuerAltNames(this.cert), this.cert.getIssuerX500Principal());
            this.serialNum = new CertificateSerialNumberImpl(this.cert.getSerialNumber());
            this.sigAlg = CryptographyUtils.findByOid(SignatureAlgorithm.class, new ASN1ObjectIdentifier(this.cert.getSigAlgOID()));
            this.subjName = new CertificateNameImpl(CertificateNameUtils.buildSubjectAltNames(this.cert), this.cert.getSubjectX500Principal());
            this.validInterval = new CertificateValidIntervalImpl(this.cert.getNotBefore(), this.cert.getNotAfter());
        } catch (Exception e) {
            this.reset();

            throw e;
        }
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
    public void setCertificate(@Nullable X509Certificate cert) throws CertificateException {
        this.cert = cert;

        this.processCertificate();
    }

    @Override
    public boolean hasCertificateHolder() {
        return (this.certHolder != null);
    }

    @Nullable
    @Override
    @Transient
    public X509CertificateHolder getCertificateHolder() {
        return this.certHolder;
    }

    @Override
    public boolean hasExtension(ASN1ObjectIdentifier oid) {
        return (this.getExtension(oid) != null);
    }

    @Nullable
    @Override
    @Transient
    public Extension getExtension(ASN1ObjectIdentifier oid) {
        return (this.hasCertificateHolder() ? this.certHolder.getExtension(oid) : null);
    }

    @Override
    public boolean hasExtensions() {
        return (this.hasCertificateHolder() && this.certHolder.hasExtensions());
    }

    @Nullable
    @Override
    @Transient
    public Extensions getExtensions() {
        return (this.hasCertificateHolder() ? this.certHolder.getExtensions() : null);
    }

    @Override
    public boolean hasIssuerName() {
        return (this.getIssuerName() != null);
    }

    @Nullable
    @Override
    @Transient
    public CertificateName getIssuerName() {
        return this.issuerName;
    }
}
