package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmission;
import javax.annotation.Nullable;

public class DiscoveryTestcaseSubmissionImpl extends AbstractToolTestcaseSubmission<DiscoveryTestcaseDescription, DiscoveryTestcase> implements
    DiscoveryTestcaseSubmission {
    private MailInfo mailInfo;

    public DiscoveryTestcaseSubmissionImpl(@Nullable DiscoveryTestcase testcase, MailInfo mailInfo) {
        super(testcase);

        this.mailInfo = mailInfo;
    }

    @Override
    public MailInfo getMailInfo() {
        return this.mailInfo;
    }
}
