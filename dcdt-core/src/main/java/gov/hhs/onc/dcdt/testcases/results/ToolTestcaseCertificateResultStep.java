package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;

public interface ToolTestcaseCertificateResultStep extends ToolTestcaseResultStep {
    public boolean hasCertificateInfo();

    @Nullable
    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(@Nullable CertificateInfo certificateInfo);

    @JsonProperty("certStatus")
    public ToolTestcaseCertificateResultType getCertificateStatus();

    public void setCertificateStatus(ToolTestcaseCertificateResultType certStatus);

    public void updateCertificateStatus(byte[] certData, MailAddress mailAddr);
}
