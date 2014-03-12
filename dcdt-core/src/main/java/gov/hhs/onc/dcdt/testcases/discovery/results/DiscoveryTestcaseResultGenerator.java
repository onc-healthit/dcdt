package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultGenerator;
import java.io.InputStream;
import java.util.List;

public interface DiscoveryTestcaseResultGenerator extends ToolTestcaseResultGenerator<DiscoveryTestcaseResultConfig, DiscoveryTestcaseResultInfo> {
    public EmailInfo generateTestcaseResult(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);

    public EmailInfo runDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases);
}
