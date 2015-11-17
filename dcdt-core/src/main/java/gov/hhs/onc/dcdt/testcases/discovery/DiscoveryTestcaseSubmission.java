package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;

public interface DiscoveryTestcaseSubmission extends ToolTestcaseSubmission<DiscoveryTestcaseDescription, DiscoveryTestcase> {
    public MailInfo getMailInfo();
}
