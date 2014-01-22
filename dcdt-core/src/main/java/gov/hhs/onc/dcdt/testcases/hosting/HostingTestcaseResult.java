package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseResultImpl;

@JsonSubTypes({ @Type(HostingTestcaseResultImpl.class) })
public interface HostingTestcaseResult extends ToolTestcaseResult {
}
