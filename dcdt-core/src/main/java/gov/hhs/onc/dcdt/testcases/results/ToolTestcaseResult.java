package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import java.util.List;

public interface ToolTestcaseResult<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>> extends
    ToolResultBean {
    public boolean hasProcessedSteps();

    public List<CertificateDiscoveryStep> getProcessedSteps();

    public boolean hasProcessingMessages();

    public List<ToolMessage> getProcessingMessages();

    public void setProcessingMessages(List<ToolMessage> procMsgs);

    public boolean isProcessingSuccess();

    public void setProcessingSuccess(boolean procSuccess);

    public V getSubmission();
}
