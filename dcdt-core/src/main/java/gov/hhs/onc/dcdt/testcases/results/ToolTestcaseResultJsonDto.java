package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import java.util.List;

public interface ToolTestcaseResultJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>, W extends ToolTestcaseResult<T, U, V>>
    extends ToolBeanJsonDto<W>, ToolResultBean {
    @JsonProperty("msgs")
    @Override
    public List<String> getMessages();

    public void setMessages(List<String> msgs);

    @JsonProperty("processedSteps")
    public List<CertificateDiscoveryStep> getProcessedSteps();

    public void setProcessedSteps(List<CertificateDiscoveryStep> processedSteps);

    @JsonProperty("submission")
    public V getSubmission();

    public void setSubmission(V submission);

    @JsonProperty("success")
    @Override
    public boolean isSuccess();

    public void setSuccess(boolean success);
}
