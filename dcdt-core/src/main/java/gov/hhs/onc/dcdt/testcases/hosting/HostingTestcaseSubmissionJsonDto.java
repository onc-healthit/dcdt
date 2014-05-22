package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.NamedBeanName;
import gov.hhs.onc.dcdt.mail.DirectAddress.MailAddress;
import gov.hhs.onc.dcdt.mail.HasMxRecord;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseSubmissionJsonDtoImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(HostingTestcaseSubmissionJsonDtoImpl.class) })
public interface HostingTestcaseSubmissionJsonDto extends ToolTestcaseSubmissionJsonDto<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission> {
    @MailAddress(message = "{dcdt.mail.validation.constraints.DirectAddress.msg}")
    @HasMxRecord
    @JsonProperty("directAddr")
    @Nullable
    public String getDirectAddress();

    public void setDirectAddress(@Nullable String directAddr);

    @NamedBeanName(HostingTestcase.class)
    @Nullable
    @Override
    public String getTestcase();
}
