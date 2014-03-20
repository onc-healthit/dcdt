package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;

public interface HostingTestcaseSubmission extends ToolBean {
    public boolean hasDirectAddress();

    @Nullable
    public MailAddress getDirectAddress();

    public void setDirectAddress(@Nullable MailAddress directAddr);

    public boolean hasHostingTestcase();

    @Nullable
    public HostingTestcase getHostingTestcase();

    public void setHostingTestcase(@Nullable HostingTestcase hostingTestcase);
}
