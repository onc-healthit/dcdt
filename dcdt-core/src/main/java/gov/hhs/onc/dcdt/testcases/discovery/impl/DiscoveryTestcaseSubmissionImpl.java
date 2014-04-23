package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmission;
import javax.annotation.Nullable;

public class DiscoveryTestcaseSubmissionImpl extends AbstractToolTestcaseSubmission<DiscoveryTestcaseDescription, DiscoveryTestcase> implements
    DiscoveryTestcaseSubmission {
    private ToolMimeMessageHelper msgHelper;

    public DiscoveryTestcaseSubmissionImpl(@Nullable DiscoveryTestcase testcase, ToolMimeMessageHelper msgHelper) {
        super(testcase);

        this.msgHelper = msgHelper;
    }

    @Override
    public ToolMimeMessageHelper getMessageHelper() {
        return this.msgHelper;
    }
}
