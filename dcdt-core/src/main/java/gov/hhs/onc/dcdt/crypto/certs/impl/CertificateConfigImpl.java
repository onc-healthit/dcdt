package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.math.BigInteger;
import javax.annotation.Nullable;

public class CertificateConfigImpl extends AbstractCryptographyDescriptor implements CertificateConfig {
    private boolean ca;
    private CertificateType certType;
    private BigInteger serialNum;
    private SignatureAlgorithm sigAlg;
    private CertificateName subj;
    private CertificateValidInterval validInterval;

    @Override
    public boolean isCertificateAuthority() {
        return this.ca;
    }

    @Override
    public void setCertificateAuthority(boolean ca) {
        this.ca = ca;
    }

    @Override
    public boolean hasCertificateType() {
        return this.certType != null;
    }

    @Nullable
    @Override
    public CertificateType getCertificateType() {
        return this.certType;
    }

    @Override
    public void setCertificateType(@Nullable CertificateType certType) {
        this.certType = certType;
    }

    @Override
    public boolean hasSerialNumber() {
        return ToolNumberUtils.isPositive(this.serialNum);
    }

    @Nullable
    @Override
    public BigInteger getSerialNumber() {
        return this.serialNum;
    }

    @Override
    public void setSerialNumber(@Nullable BigInteger serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public boolean hasSignatureAlgorithm() {
        return this.sigAlg != null;
    }

    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.sigAlg;
    }

    @Override
    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg) {
        this.sigAlg = sigAlg;
    }

    @Override
    public boolean hasSubject() {
        return this.subj != null;
    }

    @Nullable
    @Override
    public CertificateName getSubject() {
        return this.subj;
    }

    @Override
    public void setSubject(@Nullable CertificateName subj) {
        this.subj = subj;
    }

    @Override
    public boolean hasValidInterval() {
        return this.validInterval != null;
    }

    @Nullable
    @Override
    public CertificateValidInterval getValidInterval() {
        return this.validInterval;
    }

    @Override
    public void setValidInterval(@Nullable CertificateValidInterval validInterval) {
        this.validInterval = validInterval;
    }
}
