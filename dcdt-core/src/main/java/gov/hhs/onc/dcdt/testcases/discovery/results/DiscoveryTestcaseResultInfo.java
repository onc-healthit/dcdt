package gov.hhs.onc.dcdt.testcases.discovery.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;

@JsonSubTypes({ @Type(DiscoveryTestcaseResultInfoImpl.class) })
public interface DiscoveryTestcaseResultInfo extends ToolTestcaseResultInfo, ToolTestcaseResultDescription {
}
