package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.KeyUsageType;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bouncycastle.asn1.ASN1Encodable;

@MappedSuperclass
public abstract class AbstractCertificateDescriptor<T extends CertificateIntervalDescriptor> extends AbstractCryptographyDescriptor implements
    CertificateDescriptor<T> {
    protected boolean ca;
    protected CertificateType certType;
    protected Set<URI> crlDistribUris;
    protected T interval;
    protected Set<URI> issuerAccessUris;
    protected Set<KeyUsageType> keyUsages;
    protected boolean selfIssued;
    protected CertificateSerialNumber serialNum;
    protected SignatureAlgorithm sigAlg;
    protected ToolMultiValueMap<GeneralNameType, ASN1Encodable> subjAltNames;
    protected CertificateDn subjDn;

    @Override
    @Transient
    public boolean isIntermediateCertificateAuthority() {
        return (this.ca && !this.selfIssued);
    }

    @Override
    @Transient
    public boolean isRootCertificateAuthority() {
        return (this.ca && this.selfIssued);
    }

    @Override
    protected void reset() {
        this.ca = false;
        this.certType = null;
        this.crlDistribUris = null;
        this.interval = null;
        this.issuerAccessUris = null;
        this.keyUsages = null;
        this.serialNum = null;
        this.sigAlg = null;
        this.subjAltNames = null;
        this.subjDn = null;
    }

    @Override
    @Transient
    public boolean isCertificateAuthority() {
        return this.ca;
    }

    @Override
    public boolean hasCertificateType() {
        return this.certType != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateType getCertificateType() {
        return this.certType;
    }

    @Override
    public boolean hasCrlDistributionUris() {
        return !CollectionUtils.isEmpty(this.crlDistribUris);
    }

    @Nullable
    @Override
    @Transient
    public Set<URI> getCrlDistributionUris() {
        return this.crlDistribUris;
    }

    @Override
    public boolean hasInterval() {
        return this.interval != null;
    }

    @Nullable
    @Override
    @Transient
    public T getInterval() {
        return this.interval;
    }

    @Override
    public boolean hasIssuerAccessUris() {
        return !CollectionUtils.isEmpty(this.issuerAccessUris);
    }

    @Nullable
    @Override
    @Transient
    public Set<URI> getIssuerAccessUris() {
        return this.issuerAccessUris;
    }

    @Override
    public boolean hasKeyUsages() {
        return !CollectionUtils.isEmpty(this.keyUsages);
    }

    @Nullable
    @Override
    @Transient
    public Set<KeyUsageType> getKeyUsages() {
        return this.keyUsages;
    }

    @Override
    @Transient
    public boolean isSelfIssued() {
        return this.selfIssued;
    }

    @Override
    public boolean hasSerialNumber() {
        return (this.serialNum != null);
    }

    @Nullable
    @Override
    @Transient
    public CertificateSerialNumber getSerialNumber() {
        return this.serialNum;
    }

    @Override
    public boolean hasSignatureAlgorithm() {
        return this.sigAlg != null;
    }

    @Nullable
    @Override
    @Transient
    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.sigAlg;
    }

    @Override
    public boolean hasSubjectAltNames() {
        return !MapUtils.isEmpty(this.subjAltNames);
    }

    @Nullable
    @Override
    @Transient
    public ToolMultiValueMap<GeneralNameType, ASN1Encodable> getSubjectAltNames() {
        return this.subjAltNames;
    }

    @Override
    public boolean hasSubjectDn() {
        return (this.subjDn != null);
    }

    @Nullable
    @Override
    @Transient
    public CertificateDn getSubjectDn() {
        return this.subjDn;
    }
}
