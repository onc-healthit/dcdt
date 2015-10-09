package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import java.util.List;

public interface ToolTestcaseResultJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends ToolBeanJsonDto<W>, ToolResultBean {
    @JsonProperty("discoveredCertInfo")
    public CertificateInfo getDiscoveredCertificateInfo();

    public void setDiscoveredCertificateInfo(CertificateInfo discoveredCertInfo);

    @JsonProperty("invalidDiscoveredCertInfos")
    public List<CertificateInfo> getInvalidDiscoveredCertificateInfos();

    public void setInvalidDiscoveredCertificateInfos(List<CertificateInfo> invalidDiscoveredCertInfos);

    @JsonProperty("msgs")
    @Override
    public List<ToolMessage> getMessages();

    public void setMessages(List<ToolMessage> msgs);

    @JsonProperty("procSteps")
    public List<CertificateDiscoveryStep> getProcessedSteps();

    public void setProcessedSteps(List<CertificateDiscoveryStep> procSteps);

    public boolean hasProcessingMessages();

    @JsonProperty("procMsgs")
    public List<ToolMessage> getProcessingMessages();

    public void setProcessingMessages(List<ToolMessage> procMsgs);

    // @JsonProperty("submission")
    public V getSubmission();

    public void setSubmission(V submission);

    @JsonProperty("success")
    @Override
    public boolean isSuccess();

    public void setSuccess(boolean success);
}
