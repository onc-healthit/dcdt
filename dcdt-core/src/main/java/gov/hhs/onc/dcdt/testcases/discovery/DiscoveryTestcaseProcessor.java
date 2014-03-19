package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;
import java.io.InputStream;
import java.util.List;

public interface DiscoveryTestcaseProcessor extends
    ToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcaseResult, DiscoveryTestcase, DiscoveryTestcaseSubmission> {
    public MailInfo processDiscoveryTestcase(InputStream emailInStream);

    public MailInfo processDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);

    public MailInfo runDecryptionSteps(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);
}
