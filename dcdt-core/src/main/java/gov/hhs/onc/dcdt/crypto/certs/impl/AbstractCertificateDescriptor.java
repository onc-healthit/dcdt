package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;

@MappedSuperclass
public abstract class AbstractCertificateDescriptor extends AbstractCryptographyDescriptor implements CertificateDescriptor {
    protected AuthorityInformationAccess aia;
    protected boolean ca;
    protected CertificateType certType;
    protected boolean selfIssued;
    protected CertificateSerialNumber serialNum;
    protected SignatureAlgorithm sigAlg;
    protected CertificateName subjName;
    protected CertificateValidInterval validInterval;

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
        this.aia = null;
        this.ca = false;
        this.certType = null;
        this.serialNum = null;
        this.sigAlg = null;
        this.subjName = null;
        this.validInterval = null;
    }

    @Override
    public boolean hasAia() {
        return (this.aia != null);
    }

    @Nullable
    @Override
    @Transient
    public AuthorityInformationAccess getAia() {
        return this.aia;
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
    public boolean hasSubjectName() {
        return this.subjName != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateName getSubjectName() {
        return this.subjName;
    }

    @Override
    public boolean hasValidInterval() {
        return this.validInterval != null;
    }

    @Nullable
    @Override
    @Transient
    public CertificateValidInterval getValidInterval() {
        return this.validInterval;
    }
}
