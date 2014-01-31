package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

@JsonTypeName("hostingTestcaseSubmission")
public class HostingTestcaseSubmissionImpl extends AbstractToolBean implements HostingTestcaseSubmission {
    private String hostingTestcaseName;
    private MailAddress directAddr;

    @Override
    public boolean hasHostingTestcaseName() {
        return !StringUtils.isBlank(this.hostingTestcaseName);
    }

    @Nullable
    @Override
    public String getHostingTestcaseName() {
        return this.hostingTestcaseName;
    }

    @Override
    public void setHostingTestcaseName(@Nullable String hostingTestcaseName) {
        this.hostingTestcaseName = hostingTestcaseName;
    }

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
}
