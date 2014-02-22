package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultJsonDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Nullable;

@Component("hostingTestcaseResultJsonDto")
@JsonTypeName("hostingTestcaseResult")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class HostingTestcaseResultJsonDtoImpl extends
    AbstractToolTestcaseResultJsonDto<HostingTestcaseResultConfig, HostingTestcaseResultInfo, HostingTestcaseResult> implements HostingTestcaseResultJsonDto {
    private HostingTestcaseResultConfig resultConfig;
    private HostingTestcaseResultInfo resultInfo;

    public HostingTestcaseResultJsonDtoImpl() {
        super(HostingTestcaseResult.class, HostingTestcaseResultImpl.class);
    }

    @Override
    public boolean hasResultConfig() {
        return this.resultConfig != null;
    }

    @Nullable
    @Override
    public HostingTestcaseResultConfig getResultConfig() {
        return this.resultConfig;
    }

    @Override
    public void setResultConfig(@Nullable HostingTestcaseResultConfig resultConfig) {
        this.resultConfig = resultConfig;
    }

    @Override
    public boolean hasResultInfo() {
        return this.resultInfo != null;
    }

    @Nullable
    @Override
    public HostingTestcaseResultInfo getResultInfo() {
        return this.resultInfo;
    }

    @Override
    public void setResultInfo(@Nullable HostingTestcaseResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }
}
