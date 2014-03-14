package gov.hhs.onc.dcdt.testcases.discovery.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultConfigImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;

@JsonSubTypes({ @Type(DiscoveryTestcaseResultConfigImpl.class) })
public interface DiscoveryTestcaseResultConfig extends ToolTestcaseResultConfig {
}
