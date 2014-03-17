package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultJsonDtoImpl;

@JsonSubTypes({ @Type(HostingTestcaseResultJsonDtoImpl.class) })
public interface HostingTestcaseResultJsonDto extends ToolTestcaseResultJsonDto<HostingTestcaseResult> {
}
