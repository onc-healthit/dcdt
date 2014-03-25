package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;

@JsonSubTypes({ @Type(HostingTestcaseResultImpl.class) })
public interface HostingTestcaseResult extends ToolTestcaseResult {
}
