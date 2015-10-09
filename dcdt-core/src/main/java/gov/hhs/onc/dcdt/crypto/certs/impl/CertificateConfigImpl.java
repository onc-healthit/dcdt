package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;

public class CertificateConfigImpl extends AbstractCertificateDescriptor implements CertificateConfig {
    @Override
    public void setAia(@Nullable AuthorityInformationAccess aia) {
        this.aia = aia;
    }

    @Override
    public void setCertificateAuthority(boolean ca) {
        this.ca = ca;
    }

    @Override
    public void setCertificateType(@Nullable CertificateType certType) {
        this.certType = certType;
    }

    @Override
    public void setSerialNumber(@Nullable CertificateSerialNumber serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg) {
        this.sigAlg = sigAlg;
    }

    @Override
    public void setSubjectName(@Nullable CertificateName subjName) {
        this.subjName = subjName;
    }

    @Override
    public void setValidInterval(@Nullable CertificateValidInterval validInterval) {
        this.validInterval = validInterval;
    }
}
