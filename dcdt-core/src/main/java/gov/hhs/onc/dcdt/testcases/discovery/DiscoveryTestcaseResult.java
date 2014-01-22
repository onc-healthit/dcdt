package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseResultImpl;

@JsonSubTypes({ @Type(DiscoveryTestcaseResultImpl.class) })
public interface DiscoveryTestcaseResult extends ToolTestcaseResult {
}
