package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseSubmissionJsonDto;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseSubmissionJsonDto")
@JsonTypeName("hostingTestcaseSubmission")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class HostingTestcaseSubmissionJsonDtoImpl extends
    AbstractToolTestcaseSubmissionJsonDto<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission> implements HostingTestcaseSubmissionJsonDto {
    private String directAddr;

    public HostingTestcaseSubmissionJsonDtoImpl() {
        super(HostingTestcaseSubmission.class, HostingTestcaseSubmissionImpl.class);
    }

    @Nullable
    @Override
    public String getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public void setDirectAddress(@Nullable String directAddr) {
        this.directAddr = directAddr;
    }
}
