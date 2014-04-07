package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;

public interface HostingTestcaseProcessor extends
    ToolTestcaseProcessor<HostingTestcaseDescription, HostingTestcaseConfig, HostingTestcaseResult, HostingTestcase, HostingTestcaseSubmission> {
}
