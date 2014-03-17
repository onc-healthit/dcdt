package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;

public interface ToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcase<T, U>> extends ToolBean {
    public boolean hasDirectAddress();

    @Nullable
    public MailAddress getDirectAddress();

    public void setDirectAddress(@Nullable MailAddress directAddr);

    public boolean hasTestcase();

    @Nullable
    public V getTestcase();

    public void setTestcase(@Nullable V testcase);
}
