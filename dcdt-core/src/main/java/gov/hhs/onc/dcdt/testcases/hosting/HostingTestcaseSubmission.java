package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseSubmissionImpl;

import javax.annotation.Nullable;


@JsonDeserialize(as = HostingTestcaseSubmissionImpl.class)
public interface HostingTestcaseSubmission extends ToolBean {
    public boolean hasHostingTestcaseName();

    @JsonProperty("hostingTestcaseName")
    @Nullable
    public String getHostingTestcaseName();

    public void setHostingTestcaseName(@Nullable String hostingTestcaseName);

    public boolean hasDirectAddress();

    @JsonProperty("directAddr")
    @Nullable
    public MailAddress getDirectAddress();

    public void setDirectAddress(@Nullable MailAddress directAddr);
}
