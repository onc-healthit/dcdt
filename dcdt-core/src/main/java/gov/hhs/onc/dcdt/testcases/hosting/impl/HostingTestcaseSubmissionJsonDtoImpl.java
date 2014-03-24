package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseSubJsonDto")
@JsonTypeName("hostingTestcaseSub")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class HostingTestcaseSubmissionJsonDtoImpl extends AbstractToolBeanJsonDto<HostingTestcaseSubmission> implements HostingTestcaseSubmissionJsonDto {
    private String directAddr;
    private String testcase;

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

    @Nullable
    @Override
    public String getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable String testcase) {
        this.testcase = testcase;
    }
}
