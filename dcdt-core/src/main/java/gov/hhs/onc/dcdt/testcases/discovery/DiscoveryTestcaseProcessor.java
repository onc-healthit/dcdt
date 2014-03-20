package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;
import java.io.InputStream;
import java.util.List;

public interface DiscoveryTestcaseProcessor extends
    ToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcaseResult, DiscoveryTestcase, DiscoveryTestcaseSubmission> {
    public DiscoveryTestcaseResult processDiscoveryTestcase(InputStream emailInStream);

    public DiscoveryTestcaseResult processDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);

    public DiscoveryTestcaseResult runDecryptionSteps(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);
}
