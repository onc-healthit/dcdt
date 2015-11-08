package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.KeyUsageType;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.crypto.utils.GeneralNameUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.apache.commons.collections4.MapUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

@Embeddable
public class CertificateInfoImpl extends AbstractCertificateDescriptor<CertificateIntervalInfo> implements CertificateInfo {
    private X509Certificate cert;
    private X509CertificateHolder certHolder;
    private ToolMultiValueMap<GeneralNameType, ASN1Encodable> issuerAltNames;
    private CertificateDn issuerDn;

    public CertificateInfoImpl() throws CertificateException {
        this(null);
    }

    public CertificateInfoImpl(@Nullable X509Certificate cert) throws CertificateException {
        this.setCertificate(cert);
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
        this.issuerAltNames = null;
        this.issuerDn = null;
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
                throw new CertificateException(String.format(
                    "Unable to build BouncyCastle X509v3 certificate (subjDn={%s}, serialNum=%d, issuerDn={%s}) holder.", this.cert.getSubjectX500Principal()
                        .getName(), this.cert.getSerialNumber(), this.cert.getIssuerX500Principal().getName()), e);
            }

            if (this.hasExtension(Extension.authorityInfoAccess)) {
                try {
                    // noinspection ConstantConditions
                    Stream.of(
                        AuthorityInformationAccess.getInstance(
                            ASN1Primitive.fromByteArray(this.getExtension(Extension.authorityInfoAccess).getExtnValue().getOctets())).getAccessDescriptions())
                        .filter(accessDesc -> accessDesc.getAccessMethod().equals(AccessDescription.id_ad_caIssuers));
                } catch (IOException e) {
                    throw new CertificateException(
                        String.format(
                            "Unable to build BouncyCastle X509v3 certificate (subjDn={%s}, serialNum=%d, issuerDn={%s}) Authority Information Access (AIA) extension.",
                            this.cert.getSubjectX500Principal().getName(), this.cert.getSerialNumber(), this.cert.getIssuerX500Principal().getName()), e);
                }
            }

            if (this.hasExtension(Extension.keyUsage)) {
                // noinspection ConstantConditions
                KeyUsage keyUsage = KeyUsage.getInstance(this.getExtension(Extension.keyUsage).getParsedValue());

                this.keyUsages =
                    Stream.of(KeyUsageType.values()).filter(keyUsageType -> keyUsage.hasUsages(keyUsageType.getTag()))
                        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(CryptographyTaggedIdentifier::getTag))));
            }

            this.ca = new BasicConstraints(this.cert.getBasicConstraints()).isCA();
            this.certType = CertificateType.X509;
            this.interval = new CertificateIntervalInfoImpl(this.cert.getNotBefore(), this.cert.getNotAfter());
            // noinspection ConstantConditions
            this.issuerAltNames =
                (this.hasExtension(Extension.issuerAlternativeName) ? GeneralNameUtils.toMap(GeneralNames.fromExtensions(this.getExtensions(),
                    Extension.issuerAlternativeName)) : null);
            this.issuerDn = new CertificateDnImpl(this.certHolder.getIssuer());
            // noinspection ConstantConditions
            this.subjAltNames =
                (this.hasExtension(Extension.subjectAlternativeName) ? GeneralNameUtils.toMap(GeneralNames.fromExtensions(this.getExtensions(),
                    Extension.subjectAlternativeName)) : null);
            this.subjDn = new CertificateDnImpl(this.certHolder.getSubject());
            this.selfIssued = this.subjDn.equals(this.issuerDn);
            this.serialNum = new CertificateSerialNumberImpl(this.cert.getSerialNumber());
            this.sigAlg = CryptographyUtils.findByOid(SignatureAlgorithm.class, new ASN1ObjectIdentifier(this.cert.getSigAlgOID()));
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
    public boolean hasIssuerAltNames() {
        return !MapUtils.isEmpty(this.issuerAltNames);
    }

    @Nullable
    @Override
    @Transient
    public ToolMultiValueMap<GeneralNameType, ASN1Encodable> getIssuerAltNames() {
        return this.issuerAltNames;
    }

    @Override
    public boolean hasIssuerDn() {
        return (this.issuerDn != null);
    }

    @Nullable
    @Override
    @Transient
    public CertificateDn getIssuerDn() {
        return this.issuerDn;
    }
}
