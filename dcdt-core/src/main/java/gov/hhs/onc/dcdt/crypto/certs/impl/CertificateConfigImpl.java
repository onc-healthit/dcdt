package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateIntervalConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.KeyUsageType;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificateConfigImpl extends AbstractCertificateDescriptor<CertificateIntervalConfig> implements CertificateConfig {
    @Override
    public void setCertificateAuthority(boolean ca) {
        this.ca = ca;
    }

    @Override
    public void setCertificateType(@Nullable CertificateType certType) {
        this.certType = certType;
    }

    @Override
    public void setCrlDistributionUris(@Nullable Set<URI> crlDistribUris) {
        this.crlDistribUris = crlDistribUris;
    }

    @Override
    public void setInterval(@Nullable CertificateIntervalConfig interval) {
        this.interval = interval;
    }

    @Override
    public void setIssuerAccessUris(@Nullable Set<URI> issuerAccessUris) {
        this.issuerAccessUris = issuerAccessUris;
    }

    @Override
    public void setKeyUsages(@Nullable Set<KeyUsageType> keyUsages) {
        this.keyUsages = keyUsages;
    }

    @Override
    public void setSelfIssued(boolean selfIssued) {
        this.selfIssued = selfIssued;
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
    public void setSubjectAltNames(@Nullable ToolMultiValueMap<GeneralNameType, ASN1Encodable> subjAltNames) {
        this.subjAltNames = subjAltNames;
    }

    @Override
    public void setSubjectDn(@Nullable CertificateDn subjDn) {
        this.subjDn = subjDn;
    }
}
