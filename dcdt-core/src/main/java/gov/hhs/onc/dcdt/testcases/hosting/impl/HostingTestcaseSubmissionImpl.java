package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import javax.annotation.Nullable;

public class HostingTestcaseSubmissionImpl extends AbstractToolBean implements HostingTestcaseSubmission {
    private MailAddress directAddr;
    private HostingTestcase hostingTestcase;

    @Override
    public boolean hasDirectAddress() {
        return this.directAddr != null;
    }

    @Nullable
    @Override
    public MailAddress getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public void setDirectAddress(@Nullable MailAddress directAddr) {
        this.directAddr = directAddr;
    }

    @Override
    public boolean hasHostingTestcase() {
        return this.hostingTestcase != null;
    }

    @Nullable
    @Override
    public HostingTestcase getHostingTestcase() {
        return this.hostingTestcase;
    }

    @Override
    public void setHostingTestcase(@Nullable HostingTestcase hostingTestcase) {
        this.hostingTestcase = hostingTestcase;
    }
}
