package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;

public interface DiscoveryTestcaseSubmission extends ToolTestcaseSubmission<DiscoveryTestcaseDescription, DiscoveryTestcase> {
    public ToolMimeMessageHelper getMessageHelper();
}
