package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseSubJsonDto")
@JsonTypeName("hostingTestcaseSub")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class HostingTestcaseSubmissionJsonDtoImpl extends AbstractToolBeanJsonDto<HostingTestcaseSubmission> implements HostingTestcaseSubmissionJsonDto {
    private String hostingTestcase;
    private String directAddr;

    public HostingTestcaseSubmissionJsonDtoImpl() {
        super(HostingTestcaseSubmission.class, HostingTestcaseSubmissionImpl.class);
    }

    @Override
    public boolean hasHostingTestcase() {
        return !StringUtils.isBlank(this.hostingTestcase);
    }

    @Nullable
    @Override
    public String getHostingTestcase() {
        return this.hostingTestcase;
    }

    @Override
    public void setHostingTestcase(@Nullable String hostingTestcase) {
        this.hostingTestcase = hostingTestcase;
    }

    @Override
    public boolean hasDirectAddress() {
        return !StringUtils.isBlank(this.directAddr);
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
