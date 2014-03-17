package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultJsonDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hostingTestcaseResultJsonDto")
@JsonTypeName("hostingTestcaseResult")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class HostingTestcaseResultJsonDtoImpl extends AbstractToolTestcaseResultJsonDto<HostingTestcaseResult> implements HostingTestcaseResultJsonDto {
    public HostingTestcaseResultJsonDtoImpl() {
        super(HostingTestcaseResult.class, HostingTestcaseResultImpl.class);
    }
}
