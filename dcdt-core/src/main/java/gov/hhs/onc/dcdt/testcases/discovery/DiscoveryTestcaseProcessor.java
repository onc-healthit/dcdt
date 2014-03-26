package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseProcessor extends
    ToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcaseResult, DiscoveryTestcase, DiscoveryTestcaseSubmission> {
    public DiscoveryTestcaseResult process(ToolMimeMessageHelper msgHelper, @Nullable DiscoveryTestcase discoveryTestcase);
}
