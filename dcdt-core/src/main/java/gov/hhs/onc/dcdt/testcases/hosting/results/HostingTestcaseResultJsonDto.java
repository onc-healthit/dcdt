package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultJsonDtoImpl;

@JsonSubTypes({ @Type(HostingTestcaseResultJsonDtoImpl.class) })
public interface HostingTestcaseResultJsonDto extends
    ToolTestcaseResultJsonDto<HostingTestcaseDescription, HostingTestcase, HostingTestcaseSubmission, HostingTestcaseResult> {
}
