package gov.hhs.onc.dcdt.testcases.hosting.results;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultGenerator;

public interface HostingTestcaseResultGenerator extends ToolTestcaseResultGenerator<HostingTestcaseResultConfig, HostingTestcaseResultInfo> {
    public HostingTestcaseSubmission getSubmission();

    public void setSubmission(HostingTestcaseSubmission submission);
}
