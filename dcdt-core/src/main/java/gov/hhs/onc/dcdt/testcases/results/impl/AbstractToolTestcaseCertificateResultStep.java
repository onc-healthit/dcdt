package gov.hhs.onc.dcdt.testcases.results.impl;

import javax.annotation.Nullable;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;

public abstract class AbstractToolTestcaseCertificateResultStep extends AbstractToolTestcaseResultStep implements ToolTestcaseCertificateResultStep {
    private CertificateInfo certificateInfo;
    private ToolTestcaseCertificateResultType certStatus;

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
    public void updateCertificateStatus(byte[] certData) {
        CertificateInfo certificateInfo = new CertificateInfoImpl();
        this.setCertificateStatus(CertificateUtils.processCertificateData(certData, certificateInfo));

        if (this.certStatus == ToolTestcaseCertificateResultType.VALID_CERT) {
            this.certificateInfo = certificateInfo;
        }
    }
}
