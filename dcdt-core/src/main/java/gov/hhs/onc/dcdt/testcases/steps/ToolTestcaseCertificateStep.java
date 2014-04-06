package gov.hhs.onc.dcdt.testcases.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import javax.annotation.Nullable;

public interface ToolTestcaseCertificateStep extends ToolTestcaseStep {
    public boolean hasCertificateInfo();

    @Nullable
    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(@Nullable CertificateInfo certInfo);

    @JsonProperty("certStatus")
    public ToolTestcaseCertificateResultType getCertificateStatus();

    public void setCertificateStatus(ToolTestcaseCertificateResultType certStatus);

    public void updateCertificateStatus(byte[] certData, MailAddress mailAddr);
}
