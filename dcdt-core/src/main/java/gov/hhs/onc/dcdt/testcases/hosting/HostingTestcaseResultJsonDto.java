package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseResultJsonDtoImpl;

@JsonSubTypes({ @Type(HostingTestcaseResultJsonDtoImpl.class) })
public interface HostingTestcaseResultJsonDto extends ToolTestcaseResultJsonDto<HostingTestcaseResult> {
}
