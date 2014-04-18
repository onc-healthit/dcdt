package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmission;
import javax.annotation.Nullable;

public class HostingTestcaseSubmissionImpl extends AbstractToolTestcaseSubmission<HostingTestcaseDescription, HostingTestcase> implements
    HostingTestcaseSubmission {
    public HostingTestcaseSubmissionImpl(@Nullable HostingTestcase testcase, MailAddress directAddr) {
        super(testcase);

        this.directAddr = directAddr;
    }
}
