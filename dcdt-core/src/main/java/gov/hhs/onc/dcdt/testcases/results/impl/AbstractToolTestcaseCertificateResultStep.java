package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.Resource;

public abstract class AbstractToolTestcaseCertificateResultStep extends AbstractToolTestcaseResultStep implements ToolTestcaseCertificateResultStep {
    private CertificateInfo certificateInfo;
    private ToolTestcaseCertificateResultType certStatus;

    @Resource(name = "certificateValidators")
    private Set<CertificateValidator> certificateValidators;

    @Override
    public boolean hasCertificateInfo() {
        return this.certificateInfo != null;
    }

    @Nullable
    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certificateInfo;
    }

    @Override
    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo) {
        this.certificateInfo = certificateInfo;
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
        CertificateInfo certificateInfo = new CertificateInfoImpl();
        this.setCertificateStatus(CertificateUtils.processCertificateData(certData, certificateInfo, mailAddr, this.certificateValidators));

        if (this.certStatus == ToolTestcaseCertificateResultType.VALID_CERT) {
            this.certificateInfo = certificateInfo;
        }
    }
}
