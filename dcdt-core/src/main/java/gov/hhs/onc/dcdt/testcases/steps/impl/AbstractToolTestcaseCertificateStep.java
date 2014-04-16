package gov.hhs.onc.dcdt.testcases.steps.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseCertificateStep;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseCertificateStep extends AbstractToolTestcaseStep implements ToolTestcaseCertificateStep {
    private CertificateInfo certInfo;
    private ToolTestcaseCertificateResultType certStatus;

    // @formatter:off
    /*
    @Autowired
    private Set<CertificateValidator> certificateValidators;
    */
    // @formatter:on

    @Override
    public boolean hasCertificateInfo() {
        return this.certInfo != null;
    }

    @Nullable
    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certInfo;
    }

    @Override
    public void setCertificateInfo(@Nullable CertificateInfo certInfo) {
        this.certInfo = certInfo;
    }

    @Override
    public ToolTestcaseCertificateResultType getCertificateStatus() {
        return this.certStatus;
    }

    @Override
    public void setCertificateStatus(ToolTestcaseCertificateResultType certStatus) {
        this.certStatus = certStatus;
    }

    @Override
    public void updateCertificateStatus(byte[] certData, MailAddress mailAddr) {
        // TEMP: dev
        try {
            this.certInfo = new CertificateInfoImpl(CertificateUtils.readCertificate(certData, CertificateType.X509, DataEncoding.DER));
            this.certStatus = ToolTestcaseCertificateResultType.VALID_CERT;
        } catch (CryptographyException e) {
            this.certStatus = ToolTestcaseCertificateResultType.UNREADABLE_CERT_DATA;
        }

        // @formatter:off
        /*
        CertificateInfo certInfo = new CertificateInfoImpl();
        this.setCertificateStatus(ToolTestcaseCertificateUtils.processCertificateData(certData, certInfo, mailAddr, this.certificateValidators));

        if (this.certStatus == ToolTestcaseCertificateResultType.VALID_CERT) {
            this.certInfo = certInfo;
        }
        */
        // @formatter:on
    }
}
